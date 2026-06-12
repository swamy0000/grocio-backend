package com.grocio.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailsDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String unit;
    private String imageUrl;
    private String badge;
    private Integer stock;
    private String description;
    private String shelfLife;
    private String countryOfOrigin;
    private Double rating;
    
    // సజెషన్స్ కోసం లిస్ట్
    private List<RelatedProductDTO> relatedProducts; 
}