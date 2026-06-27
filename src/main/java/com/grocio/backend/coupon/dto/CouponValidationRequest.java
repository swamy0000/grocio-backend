package com.grocio.backend.coupon.dto;

import lombok.Data;

@Data
public class CouponValidationRequest {
    private String code;       // కూపన్ కోడ్
    private Long userId;       // కస్టమర్ యూజర్ ఐడీ
    private Double cartTotal;  // ప్రస్తుత కార్ట్ మొత్తం బిల్లు
}