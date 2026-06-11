package com.grocio.backend.service;

import com.grocio.backend.dto.*;
import com.grocio.backend.entity.*;
import com.grocio.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // 🟢 1. యూజర్ ఐడీ ఆధారంగా కార్ట్ డేటాని తీసుకురావడం
    public List<CartItemResponseDTO> getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        return convertToDTOList(cart.getItems());
    }

    // 🟢 2. ఫ్లట్టర్ నుండి వచ్చే పూర్తి కార్ట్ ని ఒకేసారి సింక్ (Save) చేయడం
    @Transactional
    public List<CartItemResponseDTO> syncCart(Long userId, List<Map<String, Object>> flutterCartItems) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });

        // పాత కార్ట్ ఐటెమ్స్ ని క్లియర్ చేసి కొత్తవి యాడ్ చేస్తాం
        cart.getItems().clear();

        for (Map<String, Object> item : flutterCartItems) {
            Long productId = Long.valueOf(item.get("id").toString());
            Integer quantity = Integer.valueOf(item.get("qty_count").toString());

            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
                cartItem.setQuantity(quantity);
                cart.getItems().add(cartItem);
            }
        }

        Cart updatedCart = cartRepository.save(cart);
        return convertToDTOList(updatedCart.getItems());
    }

    // 🟢 3. చెకౌట్ కి వెళ్లే ముందర లైవ్ స్టాక్ చెకింగ్ ఇంప్లిమెంటేషన్
    public CartValidationResponseDTO validateCartBeforeCheckout(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        CartValidationResponseDTO response = new CartValidationResponseDTO();
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
            // డేటాబేస్ లోని stockQuantity కన్నా కార్ట్ లోని క్వాంటిటీ ఎక్కువ ఉంటే ఆపుతాం
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

    // Entity ని Flutter కి అనుకూలమైన DTO గా మార్చే హెల్పర్ మెథడ్
    private List<CartItemResponseDTO> convertToDTOList(List<CartItem> items) {
        List<CartItemResponseDTO> dtoList = new ArrayList<>();
        for (CartItem item : items) {
            CartItemResponseDTO dto = new CartItemResponseDTO();
            dto.setId(item.getProduct().getProductId());
            dto.setName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQty(item.getProduct().getUnit());
            dto.setImg(item.getProduct().getImageUrl());
            dto.setQty_count(item.getQuantity());
            dto.setAvailableStock(item.getProduct().getStockQuantity());
            dto.setIsAvailable(item.getProduct().getStockQuantity() >= item.getQuantity());
            dtoList.add(dto);
        }
        return dtoList;
    }
}