package com.grocio.backend.financial.payment.internal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.cart.repository.CartCouponRepository;
import com.grocio.backend.cart.service.CartService;
import com.grocio.backend.coupon.service.CouponService;
import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.payment.repository.PaymentRepository;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.inventory.enums.ReservationStatus;
import com.grocio.backend.inventory.service.InventoryReservationService;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.service.OrderWebhookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentRecoveryService {

    private final PaymentRepository paymentRepository;
    private final OrderWebhookService orderWebhookService;
    private final InventoryReservationService inventoryReservationService;
    private final CouponService couponService;
    private final CartService cartService;
    private final CartCouponRepository cartCouponRepository;

    @Transactional(rollbackFor = Exception.class)
    public PaymentRecoverySummary recoverSuccessfulPayments() {
        List<Payment> successfulPayments = paymentRepository.findByStatus(PaymentStatus.SUCCESS);
        int ordersRecovered = 0;
        int reservationsRecovered = 0;
        int couponsRecovered = 0;
        int cartsRecovered = 0;
        int failures = 0;

        for (Payment payment : successfulPayments) {
            String paymentReference = payment.getPaymentReference();
            String gatewayPaymentId = payment.getGatewayPaymentId();
            Order order = payment.getOrder();
            Long orderId = order != null ? order.getOrderId() : null;
            List<PaymentRecoveryResult> paymentResults = new ArrayList<>();

            if (order == null) {
                log.warn("Skipping recovery for payment without order: paymentReference={}, gatewayPaymentId={}",
                        paymentReference, gatewayPaymentId);
                continue;
            }

            try {
                if (needsOrderRecovery(order)) {
                    orderWebhookService.completeOrderPayment(order);
                    ordersRecovered++;
                    paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                            "COMPLETE_ORDER", "Order completed successfully"));
                }
            } catch (Exception ex) {
                failures++;
                paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                        "COMPLETE_ORDER", "Failed to complete order: " + ex.getMessage()));
                log.error("Recovery failed for order completion: paymentReference={}, gatewayPaymentId={}, orderId={} ",
                        paymentReference, gatewayPaymentId, orderId, ex);
            }

            try {
                int confirmed = recoverInventoryReservations(order);
                if (confirmed > 0) {
                    reservationsRecovered += confirmed;
                    paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                            "CONFIRM_INVENTORY", "Confirmed " + confirmed + " reservation(s)"));
                }
            } catch (Exception ex) {
                failures++;
                paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                        "CONFIRM_INVENTORY", "Failed to confirm inventory: " + ex.getMessage()));
                log.error(
                        "Recovery failed for inventory confirmation: paymentReference={}, gatewayPaymentId={}, orderId={} ",
                        paymentReference, gatewayPaymentId, orderId, ex);
            }

            try {
                if (cartCouponRepository.existsById(order.getUserId())) {
                    couponService.consumeCartCouponAfterPayment(order.getUserId(), order.getOrderId());
                    couponsRecovered++;
                    paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                            "CONSUME_COUPON", "Cart coupon consumed successfully"));
                }
            } catch (Exception ex) {
                failures++;
                paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                        "CONSUME_COUPON", "Failed to consume coupon: " + ex.getMessage()));
                log.error(
                        "Recovery failed for coupon consumption: paymentReference={}, gatewayPaymentId={}, orderId={} ",
                        paymentReference, gatewayPaymentId, orderId, ex);
            }

            try {
                Cart cart = cartService.getCartByUserIdEntity(order.getUserId());
                if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
                    cartService.clearCart(order.getUserId());
                    cartsRecovered++;
                    paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                            "CLEAR_CART", "Cart cleared successfully"));
                }
            } catch (Exception ex) {
                failures++;
                paymentResults.add(new PaymentRecoveryResult(paymentReference, gatewayPaymentId, orderId,
                        "CLEAR_CART", "Failed to clear cart: " + ex.getMessage()));
                log.error("Recovery failed for cart clearing: paymentReference={}, gatewayPaymentId={}, orderId={} ",
                        paymentReference, gatewayPaymentId, orderId, ex);
            }

            paymentResults.forEach(result -> log.info(
                    "Payment recovery audit: paymentReference={}, gatewayPaymentId={}, orderId={}, recoveryAction={}, recoveryResult={}",
                    result.paymentReference(), result.gatewayPaymentId(), result.orderId(), result.action(),
                    result.result()));
        }

        return new PaymentRecoverySummary(successfulPayments.size(), ordersRecovered, reservationsRecovered,
                couponsRecovered, cartsRecovered, failures);
    }

    private boolean needsOrderRecovery(Order order) {
        if (order == null) {
            return false;
        }
        boolean paymentPending = order.getPaymentStatus() == null || !"PAID".equalsIgnoreCase(order.getPaymentStatus());
        boolean orderPending = order.getStatus() == com.grocio.backend.order.lifecycle.OrderStatus.PENDING_PAYMENT;
        return paymentPending || orderPending;
    }

    private int recoverInventoryReservations(Order order) {
        if (order == null || order.getOrderId() == null) {
            return 0;
        }

        List<InventoryReservation> reservations = inventoryReservationService
                .getReservationsByOrderId(order.getOrderId());
        if (reservations == null || reservations.isEmpty()) {
            return 0;
        }

        int confirmed = 0;
        for (InventoryReservation reservation : reservations) {
            if (reservation != null && reservation.getReservationReference() != null
                    && reservation.getStatus() == ReservationStatus.RESERVED) {
                inventoryReservationService.confirmReservation(reservation.getReservationReference());
                confirmed++;
            }
        }
        return confirmed;
    }

    public record PaymentRecoverySummary(int paymentsReviewed,
            int ordersRecovered,
            int reservationsRecovered,
            int couponsRecovered,
            int cartsRecovered,
            int failures) {
    }

    private record PaymentRecoveryResult(String paymentReference,
            String gatewayPaymentId,
            Long orderId,
            String action,
            String result) {
    }
}
