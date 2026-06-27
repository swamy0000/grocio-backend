package com.grocio.backend.home.service;

import com.grocio.backend.entity.HomeSection;
import com.grocio.backend.entity.HomeSectionProduct;
import com.grocio.backend.home.dto.HomeSectionResponse;
import com.grocio.backend.product.mapper.ProductMapper;
import com.grocio.backend.repository.HomeSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeSectionService {

    private final HomeSectionRepository homeSectionRepository;

    @Transactional(readOnly = true)
    public List<HomeSectionResponse> getSections() {

        return homeSectionRepository
                .findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::mapSection)
                .toList();

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
                                .toList())
                .build();
    }
}