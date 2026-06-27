package com.grocio.backend.order.dto;

import lombok.Data;

@Data
public class PaymentModeResponse {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private Integer displayOrder;
    private Boolean enabled;
    private Boolean comingSoon;
}