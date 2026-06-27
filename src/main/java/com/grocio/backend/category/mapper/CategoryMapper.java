package com.grocio.backend.category.mapper;

import com.grocio.backend.category.dto.CategoryResponse;
import com.grocio.backend.category.entity.Category;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryResponse toResponse(Category category) {

        CategoryResponse dto = new CategoryResponse();

        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setImageUrl(category.getImageUrl());

        return dto;
    }
}
