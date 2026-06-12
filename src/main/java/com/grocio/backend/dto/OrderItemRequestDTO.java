package com.grocio.backend.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private Long productId;
    private Integer quantity;
    private BigDecimal priceAtThatTime;
}