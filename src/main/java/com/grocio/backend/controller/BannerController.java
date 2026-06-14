package com.grocio.backend.controller;

import com.grocio.backend.dto.BannerResponse;
import com.grocio.backend.mapper.BannerMapper;
import com.grocio.backend.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "*")
public class BannerController {

    @Autowired
    private BannerRepository bannerRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getBanners() {

        List<BannerResponse> banners = bannerRepository.findActiveBanners()
                .stream()
                .map(BannerMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("banners", banners);

        return ResponseEntity.ok(response);
    }
}