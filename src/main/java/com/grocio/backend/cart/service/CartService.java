package com.grocio.backend.cart.service;

import com.grocio.backend.cart.dto.CartItemResponse;
import com.grocio.backend.cart.dto.CartValidationResponse;
import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.cart.entity.CartItem;
import com.grocio.backend.cart.repository.CartItemRepository;
import com.grocio.backend.cart.repository.CartRepository;
import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import com.grocio.backend.cart.dto.CartItemRequest;
import com.grocio.backend.cart.mapper.CartMapper;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        return CartMapper.toResponseList(cart.getItems());
    }

    @Transactional(readOnly = true)
    public Cart getCartByUserIdEntity(Long userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null || cart.getItems() == null) {
            return BigDecimal.ZERO;
        }

        return cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public List<CartItemResponse> syncCart(Long userId, List<CartItemRequest> flutterCartItems) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        cart.getItems().clear();

        for (CartItemRequest item : flutterCartItems) {
            Long productId = item.getId();
            Integer quantity = item.getQty_count();

            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cart.getItems().add(cartItem);
            }
        }

        Cart updatedCart = cartRepository.save(cart);
        return CartMapper.toResponseList(updatedCart.getItems());
    }

    @Transactional(readOnly = true)
    public CartValidationResponse validateCartBeforeCheckout(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        CartValidationResponse response = new CartValidationResponse();
        response.setCanProceed(true);
        response.setMessage("All items are available in stock!");

        List<String> outOfStockItems = new ArrayList<>();

        if (cart == null || cart.getItems().isEmpty()) {
            response.setCanProceed(false);
            response.setMessage("Your cart is empty!");
            return response;
        }

        for (CartItem item : cart.getItems()) {
            Product prod = item.getProduct();
            if (prod.getStockQuantity() < item.getQuantity()) {
                response.setCanProceed(false);
                outOfStockItems.add(prod.getName() + " (Available: " + prod.getStockQuantity() + ")");
            }
        }

        if (!response.getCanProceed()) {
            response.setOutOfStockItems(outOfStockItems);
            response.setMessage("Some items in your cart went out of stock or have limited quantity!");
        }

        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> cartItemRepository.deleteAllByCartId(cart.getCartId()));
    }

    

}
