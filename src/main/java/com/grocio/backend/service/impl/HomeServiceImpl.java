package com.grocio.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.grocio.backend.dto.HomeResponse;
import com.grocio.backend.dto.HomeSectionResponse;
import com.grocio.backend.dto.ProductMapper;
import com.grocio.backend.entity.HomeSection;
import com.grocio.backend.entity.HomeSectionProduct;
import com.grocio.backend.mapper.BannerMapper;
import com.grocio.backend.mapper.CategoryMapper;
import com.grocio.backend.repository.BannerRepository;
import com.grocio.backend.repository.CategoryRepository;
import com.grocio.backend.repository.HomeSectionRepository;
import com.grocio.backend.service.HomeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final BannerRepository bannerRepository;

    private final CategoryRepository categoryRepository;

    private final HomeSectionRepository homeSectionRepository;

    @Override
    public HomeResponse getHome() {

        List<HomeSectionResponse> sections =

                homeSectionRepository
                        .findByIsActiveTrueOrderByDisplayOrderAsc()
                        .stream()
                        .map(this::mapSection)
                        .collect(Collectors.toList());

        return HomeResponse.builder()
                .success(true)
                .banners(
                        bannerRepository.findActiveBanners()
                                .stream()
                                .map(BannerMapper::toResponse)
                                .collect(Collectors.toList()))
                .categories(
                        categoryRepository.findAllByOrderByIdAsc()
                                .stream()
                                .map(CategoryMapper::toResponse)
                                .collect(Collectors.toList()))
                .sections(sections)
                .build();

    }

    private HomeSectionResponse mapSection(HomeSection section) {

        return HomeSectionResponse.builder()
                .sectionId(section.getSectionId())
                .code(section.getCode())
                .title(section.getTitle())
                .subtitle(section.getSubtitle())
                .layoutType(section.getLayoutType())
                .products(
                        section.getProducts()
                                .stream()
                                .filter(HomeSectionProduct::getIsActive)
                                .map(HomeSectionProduct::getProduct)
                                .map(ProductMapper::toResponse)
                                .collect(Collectors.toList()))
                .build();

    }

}