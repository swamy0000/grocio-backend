package com.grocio.backend.dto;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private Long productId;
    private Integer quantity;
    private Double priceAtThatTime;
}