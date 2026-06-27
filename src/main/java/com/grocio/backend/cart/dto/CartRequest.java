package com.grocio.backend.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartRequest {
    private Long userId;
    private List<CartItemRequest> items;
}
