package com.grocio.backend.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RelatedProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String unit;
    private String imageUrl;
}