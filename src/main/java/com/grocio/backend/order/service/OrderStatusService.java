package com.grocio.backend.order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grocio.backend.order.dto.OrderResponse;
import com.grocio.backend.order.dto.OrderStatusUpdateResponse;
import com.grocio.backend.order.dto.VerifyDeliveryResponse;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.lifecycle.OrderLifecycleService;
import com.grocio.backend.order.lifecycle.OrderStatus;
import com.grocio.backend.order.mapper.OrderMapper;
import com.grocio.backend.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderRepository orderRepository;
    private final OrderHistoryService orderHistoryService;
    private final OrderNotificationService orderNotificationService;
    private final OrderLifecycleService orderLifecycleService;

    public List<OrderResponse> getOrdersByStatus(String status) {
        OrderStatus targetStatus = OrderStatus.from(status);
        return orderRepository.findByStatusOrderByOrderTimeDesc(targetStatus)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    public List<OrderResponse> getOrdersByUserIdAndStatus(Long userId, String status) {
        OrderStatus targetStatus = OrderStatus.from(status);

        return orderRepository
                .findByUserIdAndStatusOrderByOrderTimeDesc(userId, targetStatus)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    public Optional<OrderResponse> getOrderById(Long orderId) {
        return orderRepository.findById(orderId).map(OrderMapper::toResponse);
    }

    public Order getOrderEntity(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderStatusUpdateResponse updateOrderStatus(Long orderId, String newStatus, Long partnerId, String remarks) {
        OrderStatus targetStatus = OrderStatus.from(newStatus);
        String annotations = remarks != null ? remarks : "Order status updated to " + targetStatus.name();
        com.grocio.backend.order.entity.Order updatedOrder;

        switch (targetStatus) {
            case CONFIRMED:
                updatedOrder = orderLifecycleService.confirmOrder(orderId, partnerId);
                break;
            case PACKED:
                updatedOrder = orderLifecycleService.packOrder(orderId);
                break;
            case SHIPPED:
                updatedOrder = orderLifecycleService.shipOrder(orderId);
                break;
            case OUT_FOR_DELIVERY:
                updatedOrder = orderLifecycleService.markOutForDelivery(orderId);
                break;
            case DELIVERED:
                updatedOrder = orderLifecycleService.deliverOrder(orderId);
                break;
            case CANCELLED:
                updatedOrder = orderLifecycleService.cancelOrder(orderId);
                break;
            default:
                throw new IllegalArgumentException("Unsupported order status update: " + newStatus);
        }

        return OrderStatusUpdateResponse.success(updatedOrder.getStatus().name(),
                "Order status updated to " + updatedOrder.getStatus().name());
    }

    @Transactional(rollbackFor = Exception.class)
    public VerifyDeliveryResponse verifyOtpAndDeliver(Long orderId, String inputOtp) {
        com.grocio.backend.order.entity.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found!"));

        if (!order.getDeliveryOtp().equals(inputOtp)) {
            throw new RuntimeException("Invalid Delivery OTP! Please check with the customer.");
        }

        orderLifecycleService.deliverOrder(orderId);
        return VerifyDeliveryResponse.success("Order delivered successfully! Payment released.");
    }
}
