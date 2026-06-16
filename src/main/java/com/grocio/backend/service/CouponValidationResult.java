package com.grocio.backend.service;

import lombok.Data;

@Data
public class CouponValidationResult {
    private boolean success;
    private String message;
    private String couponCode;
    private double discountAmount;
    private double finalAmount;

    public static CouponValidationResult success(String code, double discount, double finalAmt) {
        CouponValidationResult res = new CouponValidationResult();
        res.setSuccess(true);
        res.setMessage("Coupon applied successfully! 🎉");
        res.setCouponCode(code);
        res.setDiscountAmount(discount);
        res.setFinalAmount(finalAmt);
        return res;
    }

    public static CouponValidationResult failure(String errorMsg) {
        CouponValidationResult res = new CouponValidationResult();
        res.setSuccess(false);
        res.setMessage(errorMsg);
        res.setDiscountAmount(0.0);
        return res;
    }
}