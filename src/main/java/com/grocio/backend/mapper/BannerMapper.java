package com.grocio.backend.mapper;

import com.grocio.backend.dto.BannerResponse;
import com.grocio.backend.entity.Banner;

public class BannerMapper {

    private BannerMapper() {
    }

    public static BannerResponse toResponse(Banner banner) {

        BannerResponse dto = new BannerResponse();

        dto.setBannerId(banner.getBannerId());
        dto.setTitle(banner.getTitle());
        dto.setSubtitle(banner.getSubtitle());
        dto.setButtonText(banner.getButtonText());
        dto.setImageUrl(banner.getImageUrl());
        dto.setRedirectType(banner.getRedirectType());
        dto.setRedirectId(banner.getRedirectId());

        return dto;
    }
}