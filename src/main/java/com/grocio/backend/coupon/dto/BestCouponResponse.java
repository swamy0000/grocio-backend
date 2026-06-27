package com.grocio.backend.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestCouponResponse {

    private boolean hasBestCoupon;
    private String couponCode;
    private Double discountAmount;
}