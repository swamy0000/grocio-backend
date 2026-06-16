package com.grocio.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponValidationResponse {
    private boolean success;          // కూపన్ అప్లై అయిందా లేదా (true/false)
    private String message;           // సక్సెస్ లేదా ఎర్రర్ మెసేజ్
    private String couponCode;        // అప్లై అయిన కూపన్ పేరు
    private double discountAmount;    // తగ్గించాల్సిన అమౌంట్ (E.g. 50.0)
    private double finalAmount;       // డిస్కౌంట్ పోను కస్టమర్ కట్టాల్సిన అమౌంట్
}