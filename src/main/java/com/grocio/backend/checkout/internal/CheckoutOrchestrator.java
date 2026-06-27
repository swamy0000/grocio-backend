package com.grocio.backend.checkout.internal;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.grocio.backend.address.dto.AddressResponse;
import com.grocio.backend.address.mapper.AddressMapper;
import com.grocio.backend.address.service.AddressService;
import com.grocio.backend.cart.dto.CartValidationResponse;
import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.cart.entity.CartItem;
import com.grocio.backend.cart.service.CartService;
import com.grocio.backend.checkout.dto.CheckoutRequest;
import com.grocio.backend.checkout.exception.CheckoutException;
import com.grocio.backend.coupon.dto.CouponValidationResult;
import com.grocio.backend.coupon.service.CouponService;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.financial.payment.service.PaymentService;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.inventory.exception.InventoryException;
import com.grocio.backend.inventory.service.InventoryReservationService;
import com.grocio.backend.order.dto.OrderItemRequest;
import com.grocio.backend.order.dto.OrderPlacementResponse;
import com.grocio.backend.order.dto.OrderRequest;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CheckoutOrchestrator {
    
    private final CartService cartService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final CouponService couponService;
    private final InventoryReservationService inventoryReservationService;
    
    @Transactional(rollbackFor = Exception.class)
    public CheckoutContext process(CheckoutRequest request) {
        CheckoutContext context = new CheckoutContext();
        context.setRequest(request);
        
        Cart cart = cartService.getCartByUserIdEntity(request.getUserId());
        context.setCart(cart);
        
        CartValidationResponse cartValidation = cartService.validateCartBeforeCheckout(request.getUserId());
        if (cartValidation == null || !Boolean.TRUE.equals(cartValidation.getCanProceed())) {
            throw new CheckoutException(
                cartValidation != null ? cartValidation.getMessage() : "Cart validation failed");
            }
            
            AddressResponse validatedAddress = addressService.getValidatedUserAddress(request.getAddressId(),
            request.getUserId());
            context.setAddress(AddressMapper.toEntity(validatedAddress));
            
            java.math.BigDecimal cartTotal = cartService.calculateCartTotal(request.getUserId());
            if (cartTotal == null) {
                cartTotal = java.math.BigDecimal.ZERO;
            }
            
            CouponValidationResult couponResult = null;
            if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
                couponResult = couponService.processCoupon(request.getCouponCode(), request.getUserId(),
                cartTotal.doubleValue());
                context.setCouponValidationResult(couponResult);
                
                if (couponResult == null || !couponResult.isSuccess()) {
                    String message = couponResult != null ? couponResult.getMessage()
                    : "Coupon validation failed";
                    throw new CheckoutException(message);
                }
                
                context.setFinalPayableAmount(java.math.BigDecimal.valueOf(couponResult.getFinalAmount()));
            } else {
                context.setFinalPayableAmount(cartTotal);
            }
            
            InventoryReservation reservation;
            try {
                reservation = inventoryReservationService.reserveInventory(cart);
                context.setInventoryReservation(reservation);
            } catch (InventoryException exception) {
                throw new CheckoutException("Inventory reservation failed: " + exception.getMessage(), exception);
            }
            
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setUserId(request.getUserId());
            orderRequest.setDeliveryAddressId(request.getAddressId());
            orderRequest.setCouponDiscount(couponResult != null ? couponResult.getDiscountAmount() : 0.0);
            orderRequest.setItemTotal(cartTotal.doubleValue());
            orderRequest.setTotalAmount(context.getFinalPayableAmount().doubleValue());
            orderRequest.setItems(cart == null ? List.of()
            : cart.getItems().stream()
            .map(this::toOrderItemRequest)
            .collect(Collectors.toList()));
            orderRequest.setPaymentMethod(request.getPaymentMethod().name());
            
            Order order;
            try {
                OrderPlacementResponse placement = orderService.placePendingOrder(orderRequest);
                if (placement == null || !placement.isSuccess()) {
                    throw new CheckoutException(placement != null ? placement.getMessage() : "Order creation failed");
                }
                
                order = orderService.getOrderEntity(placement.getOrderId());
                if (order == null) {
                    throw new CheckoutException("Order was created but could not be loaded: " + placement.getOrderId());
                }
                
                context.setOrder(order);
                reservation = inventoryReservationService.assignOrderToReservation(reservation.getReservationReference(),
                order);
                context.setInventoryReservation(reservation);
            } catch (CheckoutException exception) {
                releaseReservationSafely(reservation);
                throw exception;
            } catch (Exception exception) {
                releaseReservationSafely(reservation);
                throw new CheckoutException("Order creation failed", exception);
            }
            
            PaymentResponse paymentResponse;
            try {
                paymentResponse = paymentService.createPaymentForOrder(order, request.getPaymentMethod());
                context.setPaymentResponse(paymentResponse);
            } catch (Exception exception) {
                exception.printStackTrace();
                releaseReservationSafely(reservation);
                throw new CheckoutException(
                    "Payment creation failed: " + exception.getMessage(),
                    exception);
                }
                
                return context;
            }
            
            private void releaseReservationSafely(InventoryReservation reservation) {
                if (reservation == null || reservation.getReservationReference() == null) {
                    return;
                }
                
                try {
                    inventoryReservationService.releaseReservation(reservation.getReservationReference());
                } catch (Exception ignored) {
                    // reservation release is best-effort during checkout rollback
                }
            }
            
            private OrderItemRequest toOrderItemRequest(CartItem item) {
                OrderItemRequest orderItem = new OrderItemRequest();
                orderItem.setProductId(item.getProduct().getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setPriceAtThatTime(item.getProduct().getPrice());
                return orderItem;
            }
        }
        