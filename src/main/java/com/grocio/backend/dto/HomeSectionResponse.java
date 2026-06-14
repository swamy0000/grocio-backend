package com.grocio.backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeSectionResponse {

    private Long sectionId;

    private String code;

    private String title;

    private String subtitle;

    private String layoutType;

    private List<ProductResponse> products;

}