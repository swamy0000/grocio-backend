package com.grocio.backend.order.service;

import com.grocio.backend.order.dto.OrderRequest;
import com.grocio.backend.order.dto.OrderResponse;
import com.grocio.backend.order.dto.OrderStatusUpdateResponse;
import com.grocio.backend.order.dto.PaymentModeResponse;
import com.grocio.backend.order.dto.VerifyDeliveryResponse;
import com.grocio.backend.order.dto.OrderPlacementResponse;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.service.OrderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * OrderFacadeService acts as a single entry point for all order-related
 * operations.
 * It orchestrates and delegates requests to specialized order services:
 * - PaymentModeService: Payment mode management
 * - OrderPlacementService: Order placement logic
 * - OrderPaymentService: Payment processing
 * - OrderStatusService: Order status management
 * - OrderHistoryService: Order history and user order tracking
 * - InventoryService: Stock management
 * - OrderNotificationService: WebSocket notifications
 */
@Service
@RequiredArgsConstructor
public class OrderFacadeService implements OrderService {

    private final PaymentModeService paymentModeService;
    private final OrderPlacementService orderPlacementService;
    private final OrderStatusService orderStatusService;
    private final OrderHistoryService orderHistoryService;

    /**
     * Get all active payment modes
     */
    public List<PaymentModeResponse> getActivePaymentModes() {
        return paymentModeService.getActivePaymentModes();
    }

    /**
     * Place a new order with validation, payment processing, and history recording
     */
    public OrderPlacementResponse placeOrder(OrderRequest request) {
        return orderPlacementService.placeOrder(request);
    }

    @Deprecated
    public OrderPlacementResponse placeOrderWithPendingPayment(OrderRequest request) {
        return orderPlacementService.placeOrder(request, true);
    }

    public OrderPlacementResponse placePendingOrder(OrderRequest request) {
        return placeOrderWithPendingPayment(request);
    }

    /**
     * Get orders by status
     */
    public List<OrderResponse> getOrdersByStatus(String status) {
        return orderStatusService.getOrdersByStatus(status);
    }

    public List<OrderResponse> getOrdersByUserIdAndStatus(Long userId, String status) {
        return orderStatusService.getOrdersByUserIdAndStatus(userId, status);
    }

    /**
     * Get order by ID
     */
    public Optional<OrderResponse> getOrderById(Long orderId) {
        return orderStatusService.getOrderById(orderId);
    }

    public Order getOrderEntity(Long orderId) {
        return orderStatusService.getOrderEntity(orderId);
    }

    /**
     * Get all orders for a specific user
     */
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderHistoryService.getOrdersByUserId(userId);
    }

    /**
     * Update order status and notify via WebSocket
     */
    public OrderStatusUpdateResponse updateOrderStatus(Long orderId, String status, Long partnerId, String remarks) {
        return orderStatusService.updateOrderStatus(orderId, status, partnerId, remarks);
    }

    /**
     * Verify delivery OTP and mark order as delivered
     */
    public VerifyDeliveryResponse verifyOtpAndDeliver(Long orderId, String inputOtp) {
        return orderStatusService.verifyOtpAndDeliver(orderId, inputOtp);
    }
}
