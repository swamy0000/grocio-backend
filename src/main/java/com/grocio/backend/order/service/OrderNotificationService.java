package com.grocio.backend.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * OrderNotificationService handles all WebSocket messaging for order updates
 */
@Service
@RequiredArgsConstructor
public class OrderNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Notify about new order placement to store
     */
    public void notifyNewOrderPlaced() {
        messagingTemplate.convertAndSend("/topic/store/orders", "NEW_ORDER_PLACED");
    }

    /**
     * Notify user and store about order status update
     * @param userId ID of the user
     * @param orderId ID of the order
     * @param status New order status
     */
    public void notifyOrderStatusUpdate(Long userId, Long orderId, String status) {
        Map<String, String> wsPayload = Map.of(
                "status", status,
                "orderId", String.valueOf(orderId)
        );

        messagingTemplate.convertAndSend("/topic/user/" + userId + "/orders", wsPayload);
        messagingTemplate.convertAndSend("/topic/order/" + orderId, wsPayload);
    }

    /**
     * Notify user and store about order delivery
     * @param userId ID of the user
     * @param orderId ID of the order
     */
    public void notifyOrderDelivered(Long userId, Long orderId) {
        messagingTemplate.convertAndSend("/topic/user/" + userId + "/orders", "DELIVERED");
        messagingTemplate.convertAndSend("/topic/order/" + orderId, "DELIVERED");
    }
}
