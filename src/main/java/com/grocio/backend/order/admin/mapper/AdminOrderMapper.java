package com.grocio.backend.order.admin.mapper;

import com.grocio.backend.order.admin.dto.OrderStatusUpdateResponse;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.lifecycle.OrderStatus;

import java.time.LocalDateTime;

public class AdminOrderMapper {

    private AdminOrderMapper() {
    }

    public static OrderStatusUpdateResponse toResponse(Order order, OrderStatus previousStatus, String message) {
        return OrderStatusUpdateResponse.builder()
                .orderId(order.getOrderId())
                .previousStatus(previousStatus)
                .currentStatus(order.getStatus())
                .updatedAt(LocalDateTime.now())
                .message(message)
                .build();
    }
}
