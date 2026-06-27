package com.grocio.backend.cart.dto;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long id;
    private Integer qty_count;
}
