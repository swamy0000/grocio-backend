
package com.grocio.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {

    private final ProductRepository productRepository;

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Product> products = productRepository.findBySubCategory_IdAndIsActiveTrue(categoryId);

            response.put("success", true);
            response.put("products", products);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to load products for this category.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}