package com.grocio.backend.category.service;

import com.grocio.backend.category.dto.CategoryResponse;
import com.grocio.backend.category.dto.SubCategoryResponse;
import com.grocio.backend.category.mapper.CategoryMapper;
import com.grocio.backend.category.repository.CategoryRepository;
import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.repository.ProductRepository;
import com.grocio.backend.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
            SubCategoryRepository subCategoryRepository,
            ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public List<CategoryResponse> getAllByOrderByIdAs() {
        return categoryRepository.findAllByOrderByIdAsc()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    public List<SubCategoryResponse> getSubCategories(Long categoryId) {
        return subCategoryRepository
                .findByCategory_IdOrderByDisplayOrderAsc(categoryId)
                .stream()
                .map(subCategory -> {
                    SubCategoryResponse dto = new SubCategoryResponse();
                    dto.setId(subCategory.getId());
                    dto.setName(subCategory.getName());
                    dto.setImageUrl(subCategory.getImageUrl());
                    dto.setDisplayOrder(subCategory.getDisplayOrder());
                    return dto;
                })
                .toList();
    }

    public Map<String, Object> getProductsByCategory(Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productRepository.findBySubCategory_IdAndIsActiveTrue(categoryId);
            response.put("success", true);
            response.put("products", products);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to load products for this category.");
            return response;
        }
    }
}
