package com.grocio.backend.order.lifecycle;

import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.repository.OrderRepository;
import com.grocio.backend.order.service.OrderHistoryService;
import com.grocio.backend.order.service.OrderNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderLifecycleService {

    private final OrderRepository orderRepository;
    private final OrderStateValidator orderStateValidator;
    private final OrderHistoryService orderHistoryService;
    private final OrderNotificationService orderNotificationService;

    @Transactional(rollbackFor = Exception.class)
    public Order confirmOrder(Long orderId, Long partnerId) {
        return transitionOrderStatus(orderId, OrderStatus.CONFIRMED, "Order confirmed.", partnerId);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order packOrder(Long orderId) {
        return transitionOrderStatus(orderId, OrderStatus.PACKED, "Order packed and ready for shipment.", null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order shipOrder(Long orderId) {
        return transitionOrderStatus(orderId, OrderStatus.SHIPPED, "Order shipped.", null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order markOutForDelivery(Long orderId) {
        return transitionOrderStatus(orderId, OrderStatus.OUT_FOR_DELIVERY, "Order is out for delivery.", null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order deliverOrder(Long orderId) {
        return transitionOrderStatus(orderId, OrderStatus.DELIVERED, "Order delivered.", null);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order cancelOrder(Long orderId) {
        return transitionOrderStatus(orderId, OrderStatus.CANCELLED, "Order cancelled.", null);
    }

    private Order transitionOrderStatus(Long orderId, OrderStatus targetStatus, String remarks, Long partnerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        OrderStatus currentStatus = order.getStatus();
        if (currentStatus == null) {
            throw new IllegalStateException("Order status is not initialized for order " + orderId);
        }

        orderStateValidator.validate(currentStatus, targetStatus);
        order.setStatus(targetStatus);

        if (partnerId != null) {
            order.setDeliveryPartnerId(partnerId);
        }

        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        orderHistoryService.recordOrderHistory(orderId, currentStatus, targetStatus, null, remarks);

        if (OrderStatus.DELIVERED.equals(targetStatus)) {
            orderNotificationService.notifyOrderDelivered(savedOrder.getUserId(), orderId);
        } else {
            orderNotificationService.notifyOrderStatusUpdate(savedOrder.getUserId(), orderId,
                    savedOrder.getStatus().name());
        }

        return savedOrder;
    }
}
