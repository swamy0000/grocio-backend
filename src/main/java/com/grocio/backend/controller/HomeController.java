package com.grocio.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.grocio.backend.dto.HomeResponse;
import com.grocio.backend.service.HomeService;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@CrossOrigin
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public HomeResponse getHome() {

        return homeService.getHome();

    }

}