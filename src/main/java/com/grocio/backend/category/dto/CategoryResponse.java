package com.grocio.backend.category.dto;

import lombok.Data;

@Data
public class CategoryResponse {

    private Long id;
    private String name;
    private String imageUrl;

}
