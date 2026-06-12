package com.grocio.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    private Long productId;
    private String name;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String unit;
    private String imageUrl;
    private String badge;
    private Double rating;

}