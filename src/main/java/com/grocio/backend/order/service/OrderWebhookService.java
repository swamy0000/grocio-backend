package com.grocio.backend.order.service;

import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.lifecycle.OrderLifecycleService;
import com.grocio.backend.order.lifecycle.OrderStatus;
import com.grocio.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderWebhookService {

    private final OrderRepository orderRepository;
    private final OrderHistoryService orderHistoryService;
    private final OrderNotificationService orderNotificationService;
    private final OrderLifecycleService orderLifecycleService;

    @Transactional(rollbackFor = Exception.class)
    public void completeOrderPayment(Order order) {
        if (order == null || order.getOrderId() == null) {
            throw new OrderNotFoundException("Order must be provided to complete payment");
        }

        if (!"PAID".equalsIgnoreCase(order.getPaymentStatus())) {
            order.setPaymentStatus("PAID");
        }

        if (OrderStatus.PENDING_PAYMENT.equals(order.getStatus())) {
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
            orderLifecycleService.confirmOrder(order.getOrderId(), null);
            return;
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        orderHistoryService.recordOrderHistory(order.getOrderId(), order.getStatus(), order.getStatus(), null,
                "Payment captured by Razorpay and order completed.");
        orderNotificationService.notifyOrderStatusUpdate(order.getUserId(), order.getOrderId(),
                order.getStatus().name());
    }
}
