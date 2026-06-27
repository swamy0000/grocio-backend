package com.grocio.backend.cart.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private Long userId;
    private List<Object> items;
}
