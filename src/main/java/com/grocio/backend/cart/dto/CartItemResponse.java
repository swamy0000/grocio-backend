package com.grocio.backend.cart.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemResponse {
    private Long id; // product_id
    private String name;
    private BigDecimal price;
    private String qty; // unit (e.g. 1 kg)
    private String img; // imageUrl
    private Integer qty_count; // quantity selected
    private Integer availableStock;
    private Boolean isAvailable; // stock availability for Flutter
}
