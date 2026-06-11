package com.grocio.backend.dto;

import lombok.Data;

@Data
public class RelatedProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Double oldPrice;
    private String unit;
    private String imageUrl;
}