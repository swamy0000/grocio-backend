package com.grocio.backend.category.dto;

import lombok.Data;

@Data
public class SubCategoryResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private Integer displayOrder;

}
