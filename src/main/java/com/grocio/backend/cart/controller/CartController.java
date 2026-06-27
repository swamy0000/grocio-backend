package com.grocio.backend.cart.controller;

import com.grocio.backend.cart.dto.CartItemRequest;
import com.grocio.backend.cart.dto.CartItemResponse;
import com.grocio.backend.cart.dto.CartValidationResponse;
import com.grocio.backend.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }
    
    @PostMapping("/sync/{userId}")
    public ResponseEntity<List<CartItemResponse>> syncCart(@PathVariable Long userId, @RequestBody List<CartItemRequest> items) {
        return ResponseEntity.ok(cartService.syncCart(userId, items));
    }
    
    @GetMapping("/validate/{userId}")
    public ResponseEntity<CartValidationResponse> validateCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.validateCartBeforeCheckout(userId));
    }
}
