package com.grocio.backend.controller;

import com.grocio.backend.dto.ProductDetailsDTO;
import com.grocio.backend.entity.Product;
import com.grocio.backend.repository.ProductRepository;
import com.grocio.backend.service.ProductService;

import java.util.List;

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
}