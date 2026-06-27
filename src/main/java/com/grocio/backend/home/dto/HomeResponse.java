package com.grocio.backend.home.dto;

import com.grocio.backend.dto.BannerResponse;
import com.grocio.backend.category.dto.CategoryResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeResponse {

    private boolean success;

    private List<BannerResponse> banners;

    private List<CategoryResponse> categories;

    private List<HomeSectionResponse> sections;
}