package com.grocio.backend.product.controller;

import com.grocio.backend.product.dto.ProductDetailsDTO;
import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.service.ProductService;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{subCategoryId}/products")
    public List<Product> getProducts(@PathVariable Long subCategoryId) {
        return productService.getProductsBySubCategory(subCategoryId);
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
        Map<String, Object> response = productService.getBestSellers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getProductsByCategory(@PathVariable Long categoryId) {
        Map<String, Object> response = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(response);
    }
}
