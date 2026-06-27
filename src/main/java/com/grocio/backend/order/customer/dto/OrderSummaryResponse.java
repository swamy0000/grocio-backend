package com.grocio.backend.order.customer.dto;

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
public class OrderSummaryResponse {
    private Long orderId;
    private OrderStatus status;
    private Double totalAmount;
    private LocalDateTime createdAt;
}
