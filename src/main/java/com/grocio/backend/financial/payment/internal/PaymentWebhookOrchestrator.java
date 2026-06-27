package com.grocio.backend.financial.payment.internal;

import com.grocio.backend.financial.payment.dto.PaymentWebhookEntity;
import com.grocio.backend.financial.payment.dto.PaymentWebhookPayment;
import com.grocio.backend.financial.payment.dto.PaymentWebhookPayload;
import com.grocio.backend.financial.payment.dto.PaymentWebhookRequest;
import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.payment.exception.PaymentException;
import com.grocio.backend.financial.payment.gateway.client.RazorpayGatewayClient;
import com.grocio.backend.coupon.service.CouponService;
import com.grocio.backend.financial.payment.service.PaymentWebhookService;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.inventory.enums.ReservationStatus;
import com.grocio.backend.inventory.service.InventoryReservationService;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.service.OrderWebhookService;
import com.grocio.backend.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentWebhookOrchestrator {

    private final RazorpayGatewayClient razorpayGatewayClient;
    private final PaymentWebhookService paymentWebhookService;
    private final OrderWebhookService orderWebhookService;
    private final InventoryReservationService inventoryReservationService;
    private final CouponService couponService;
    private final CartService cartService;

    @Transactional(rollbackFor = Exception.class)
    public void handleWebhook(String payload, String signature) {
        if (payload == null || payload.isBlank()) {
            throw new PaymentException("Webhook payload must not be blank");
        }
        if (signature == null || signature.isBlank()) {
            throw new PaymentException("Webhook signature header is missing");
        }

        boolean verified = razorpayGatewayClient.verifyWebhookSignature(payload, signature);
        if (!verified) {
            throw new PaymentException("Invalid webhook signature");
        }

        PaymentWebhookRequest webhookRequest = razorpayGatewayClient.parseWebhookPayload(payload);
        if (webhookRequest == null || webhookRequest.getEvent() == null) {
            throw new PaymentException("Invalid webhook payload: missing event");
        }

        if (!"payment.captured".equalsIgnoreCase(webhookRequest.getEvent())) {
            log.info("Ignoring unsupported webhook event: {}", webhookRequest.getEvent());
            return;
        }

        PaymentWebhookPayload payloadWrapper = webhookRequest.getPayload();
        if (payloadWrapper == null || payloadWrapper.getPayment() == null
                || payloadWrapper.getPayment().getEntity() == null) {
            throw new PaymentException("Invalid webhook payload: payment entity missing");
        }

        PaymentWebhookEntity paymentEntity = payloadWrapper.getPayment().getEntity();
        String gatewayOrderId = paymentEntity.getOrderId();
        String gatewayPaymentId = paymentEntity.getId();

        if (gatewayOrderId == null || gatewayOrderId.isBlank() || gatewayPaymentId == null
                || gatewayPaymentId.isBlank()) {
            throw new PaymentException("Invalid webhook payload: missing order_id or payment id");
        }

        Payment payment = paymentWebhookService
                .findByGatewayOrderOrPaymentId(gatewayOrderId.trim(), gatewayPaymentId.trim())
                .orElseThrow(() -> new PaymentException("Payment not found for gateway order: " + gatewayOrderId));

        if (payment.getStatus() == com.grocio.backend.financial.shared.enums.PaymentStatus.SUCCESS) {
            return;
        }

        paymentWebhookService.completeGatewayPayment(payment, gatewayPaymentId.trim());

        Order order = payment.getOrder();
        if (order == null) {
            throw new PaymentException("Associated order not found for payment: " + payment.getPaymentReference());
        }

        orderWebhookService.completeOrderPayment(order);
        couponService.consumeCartCouponAfterPayment(order.getUserId(), order.getOrderId());
        confirmInventoryReservation(order);
        cartService.clearCart(order.getUserId());
    }

    private void confirmInventoryReservation(Order order) {
        if (order == null || order.getOrderId() == null) {
            return;
        }

        List<InventoryReservation> reservations = inventoryReservationService
                .getReservationsByOrderId(order.getOrderId());
        if (reservations == null || reservations.isEmpty()) {
            return;
        }

        for (InventoryReservation reservation : reservations) {
            if (reservation != null && reservation.getReservationReference() != null
                    && reservation.getStatus() == ReservationStatus.RESERVED) {
                inventoryReservationService.confirmReservation(reservation.getReservationReference());
            }
        }
    }
}
