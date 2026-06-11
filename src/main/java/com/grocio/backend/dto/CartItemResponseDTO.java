package com.grocio.backend.dto;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long id; // product_id
    private String name;
    private Double price;
    private String qty; // unit (e.g. 1 kg)
    private String img; // imageUrl
    private Integer qty_count; // quantity selected
    private Integer availableStock;
    private Boolean isAvailable; // 🟢 స్టాక్ ఉందో లేదో ఫ్లట్టర్ కి చెప్పడానికి
}