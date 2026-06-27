package com.grocio.backend.order.customer.dto;

import com.grocio.backend.address.dto.AddressResponse;
import com.grocio.backend.order.lifecycle.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsResponse {
    private Long orderId;
    private OrderStatus status;
    private String paymentStatus;
    private AddressResponse deliveryAddress;
    private Double totalAmount;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}
