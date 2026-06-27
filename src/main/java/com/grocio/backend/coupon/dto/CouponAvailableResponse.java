package com.grocio.backend.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponAvailableResponse {

    private String code;
    private String title;
    private String description;

    private String discountType;
    private Double discountValue;

    private Double minimumOrderAmount;

    private boolean applicable;
    private String reason;
}