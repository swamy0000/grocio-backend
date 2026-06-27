package com.grocio.backend.home.service;

import com.grocio.backend.dto.BannerResponse;
import com.grocio.backend.mapper.BannerMapper;
import com.grocio.backend.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeBannerService {

    private final BannerRepository bannerRepository;

    public List<BannerResponse> getActiveBanners() {
        return bannerRepository.findActiveBanners()
                .stream()
                .map(BannerMapper::toResponse)
                .toList();
    }
}