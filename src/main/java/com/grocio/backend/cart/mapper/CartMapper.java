package com.grocio.backend.cart.mapper;

import com.grocio.backend.cart.dto.CartItemResponse;
import com.grocio.backend.cart.dto.CartRequest;
import com.grocio.backend.cart.dto.CartResponse;
import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.cart.entity.CartItem;

import java.util.List;
import java.util.stream.Collectors;

public class CartMapper {

    private CartMapper() {}

    public static List<CartItemResponse> toResponseList(List<CartItem> items) {
        return items.stream().map(CartItemMapper::toResponse).collect(Collectors.toList());
    }

    public static Cart toEntity(CartRequest req) {
        Cart c = new Cart();
        c.setUserId(req.getUserId());
        return c;
    }

    public static CartResponse toResponse(Cart c) {
        CartResponse r = new CartResponse();
        r.setUserId(c.getUserId());
        return r;
    }
}
