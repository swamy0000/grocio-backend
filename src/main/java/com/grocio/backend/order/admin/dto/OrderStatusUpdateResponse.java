package com.grocio.backend.order.admin.dto;

import com.grocio.backend.order.lifecycle.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateResponse {
    private Long orderId;
    private OrderStatus previousStatus;
    private OrderStatus currentStatus;
    private LocalDateTime updatedAt;
    private String message;
}
