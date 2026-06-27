package com.grocio.backend.home.dto;

import com.grocio.backend.product.dto.ProductResponse;
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