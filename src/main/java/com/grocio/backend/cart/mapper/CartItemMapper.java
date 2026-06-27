package com.grocio.backend.cart.mapper;

import com.grocio.backend.cart.dto.CartItemResponse;
import com.grocio.backend.cart.entity.CartItem;

public class CartItemMapper {

    private CartItemMapper() {}

    public static CartItemResponse toResponse(CartItem item) {
        CartItemResponse dto = new CartItemResponse();
        dto.setId(item.getProduct().getProductId());
        dto.setName(item.getProduct().getName());
        dto.setPrice(item.getProduct().getPrice());
        dto.setQty(item.getProduct().getUnit());
        dto.setImg(item.getProduct().getImageUrl());
        dto.setQty_count(item.getQuantity());
        dto.setAvailableStock(item.getProduct().getStockQuantity());
        dto.setIsAvailable(item.getProduct().getStockQuantity() >= item.getQuantity());
        return dto;
    }
}
