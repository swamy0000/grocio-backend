package com.grocio.backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponse {

    private Long bannerId;
    private String title;
    private String subtitle;
    private String buttonText;
    private String imageUrl;
    private String redirectType;
    private Long redirectId;
}