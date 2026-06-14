package com.grocio.backend.dto;

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