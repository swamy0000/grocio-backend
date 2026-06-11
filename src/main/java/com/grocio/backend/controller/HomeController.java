package com.grocio.backend.controller;

import com.grocio.backend.repository.CategoryRepository;
import com.grocio.backend.repository.ProductRepository;
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

    @Autowired
    private ProductRepository productRepository;

    // 🟢 మన Home API: http://localhost:8089/api/home/data
    @GetMapping("/data")
    public ResponseEntity<Map<String, Object>> getHomeData() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // కేటగిరీలు మరియు ప్రొడక్ట్స్ ని ఒకేసారి పంపిస్తున్నాం
            response.put("success", true);
            response.put("categories", categoryRepository.findAll());
            response.put("products", productRepository.findAll());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch home data");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}