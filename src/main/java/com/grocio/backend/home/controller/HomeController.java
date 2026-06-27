package com.grocio.backend.home.controller;
import com.grocio.backend.home.dto.HomeResponse;
import com.grocio.backend.home.service.HomeFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@CrossOrigin
public class HomeController {

    private final HomeFacadeService homeFacadeService;

    @GetMapping
    public HomeResponse getHome() {
        return homeFacadeService.getHome();
    }
}