package com.grocio.backend.product.mapper;

import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.dto.ProductResponse;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {

        ProductResponse dto = new ProductResponse();

        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setOldPrice(product.getOldPrice());
        dto.setUnit(product.getUnit());
        dto.setImageUrl(product.getImageUrl());
        dto.setBadge(product.getBadge());
        dto.setRating(product.getRating());

        return dto;
    }
}
