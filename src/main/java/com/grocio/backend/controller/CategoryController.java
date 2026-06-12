package com.grocio.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocio.backend.entity.Category;
import com.grocio.backend.entity.Product;
import com.grocio.backend.entity.SubCategory;
import com.grocio.backend.repository.CategoryRepository;
import com.grocio.backend.repository.ProductRepository;
import com.grocio.backend.repository.SubCategoryRepository;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*") // CORS ఎర్రర్ రాకుండా
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    // 🟢 1. కేవలం కేటగిరీల లిస్ట్ మాత్రమే తెచ్చుకోవడానికి
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/{categoryId}/subcategories")
    public List<SubCategory> getSubCategories(
            @PathVariable Long categoryId) {

        return subCategoryRepository
                .findByCategory_IdOrderByDisplayOrderAsc(categoryId);
    }

    // 🟢 2. ఒక కేటగిరీ (ఉదా: 1) మీద క్లిక్ చేస్తే, అందులోని ప్రొడక్ట్స్ తెచ్చే API
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