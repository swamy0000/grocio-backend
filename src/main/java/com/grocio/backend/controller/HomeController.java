package com.grocio.backend.controller;

import com.grocio.backend.dto.CategoryResponse;
import com.grocio.backend.repository.CategoryRepository;
import com.grocio.backend.repository.ProductRepository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/home")
@CrossOrigin(origins = "*") // CORS ఎర్రర్ రాకుండా
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getHomeData() {
        Map<String, Object> response = new HashMap<>();

        try {
            // కేటగిరీలు మరియు ప్రొడక్ట్స్ ని ఒకేసారి పంపిస్తున్నాం
            response.put("success", true);
            List<CategoryResponse> categories = categoryRepository.findAll()
                    .stream()
                    .map(category -> {

                        CategoryResponse dto = new CategoryResponse();

                        dto.setId(category.getId());
                        dto.setName(category.getName());
                        dto.setImageUrl(category.getImageUrl());

                        return dto;

                    }).toList();

            response.put("categories", categories);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch home data");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
