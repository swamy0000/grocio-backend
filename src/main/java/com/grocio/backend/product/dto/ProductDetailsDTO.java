package com.grocio.backend.product.dto;

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

    private List<RelatedProductDTO> relatedProducts;
}
