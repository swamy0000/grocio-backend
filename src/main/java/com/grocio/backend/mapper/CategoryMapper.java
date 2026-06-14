package com.grocio.backend.mapper;

import org.springframework.stereotype.Component;

import com.grocio.backend.dto.CategoryResponse;
import com.grocio.backend.entity.Category;

import lombok.*;

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