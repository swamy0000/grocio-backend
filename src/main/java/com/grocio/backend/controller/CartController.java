package com.grocio.backend.controller;

import com.grocio.backend.dto.*;
import com.grocio.backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. పాత కార్ట్ లోడ్ చేయడానికి (GET /api/cart/1)
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponseDTO>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    // 2. కార్ట్ అప్‌డేట్ / సింక్ చేయడానికి (POST /api/cart/sync/1)
    @PostMapping("/sync/{userId}")
    public ResponseEntity<List<CartItemResponseDTO>> syncCart(@PathVariable Long userId, @RequestBody List<Map<String, Object>> items) {
        return ResponseEntity.ok(cartService.syncCart(userId, items));
    }

    // 3. చెకౌట్ ముందర లైవ్ స్టాక్ వెరిఫై చేయడానికి (GET /api/cart/validate/1)
    @GetMapping("/validate/{userId}")
    public ResponseEntity<CartValidationResponseDTO> validateCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.validateCartBeforeCheckout(userId));
    }
}