package com.grocio.backend.controller;

import com.grocio.backend.dto.ProductDetailsDTO;
import com.grocio.backend.dto.ProductMapper;
import com.grocio.backend.dto.ProductResponse;
import com.grocio.backend.entity.Product;
import com.grocio.backend.repository.ProductRepository;
import com.grocio.backend.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{subCategoryId}/products")
    public List<Product> getProducts(
            @PathVariable Long subCategoryId) {
        return productRepository
                .findBySubCategory_IdAndIsActiveTrue(subCategoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long id) {
        try {
            ProductDetailsDTO productDetails = productService.getProductDetailsWithSuggestions(id);
            return ResponseEntity.ok(productDetails);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("{\"error\": \"Product not found\"}");
        }
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<Map<String, Object>> getBestSellers() {

        List<ProductResponse> products = productRepository
                .findTop10ByIsActiveTrueOrderByProductIdDesc()
                .stream()
                .map(product -> {

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

                }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("products", products);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(
            @PathVariable Long categoryId) {

        List<ProductResponse> products = productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("products", products);

        return ResponseEntity.ok(response);
    }
}