package com.grocio.backend.home.service;

import com.grocio.backend.category.service.CategoryService;
import com.grocio.backend.home.dto.HomeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeFacadeService {

    private final HomeBannerService bannerService;
    private final HomeSectionService sectionService;
    private final CategoryService categoryService;

    public HomeResponse getHome() {

        return HomeResponse.builder()
                .success(true)
                .banners(bannerService.getActiveBanners())
                .categories(categoryService.getAllByOrderByIdAs())
                .sections(sectionService.getSections())
                .build();
    }
}