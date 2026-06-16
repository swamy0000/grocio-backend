package com.grocio.backend.controller;

import com.grocio.backend.dto.CouponValidationRequest;
import com.grocio.backend.dto.CouponValidationResponse;
import com.grocio.backend.service.CouponService;
import com.grocio.backend.service.CouponValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/validate")
    public ResponseEntity<CouponValidationResponse> validateCoupon(@RequestBody CouponValidationRequest request) {
        
        // 🟢 సర్వీస్ లేయర్ కి డేటాను పంపి ప్రాసెస్ చేయడం (No business logic here)
        CouponValidationResult result = couponService.processCoupon(
                request.getCode(), 
                request.getUserId(), 
                request.getCartTotal()
        );

        // 🟢 ప్రొఫెషనల్ DTO రెస్పాన్స్ బిల్డ్ చేయడం
        CouponValidationResponse response = new CouponValidationResponse(
                result.isSuccess(),
                result.getMessage(),
                result.getCouponCode(),
                result.getDiscountAmount(),
                result.getFinalAmount()
        );

        // కూపన్ ఫెయిల్ అయితే Bad Request (400) లేదా సక్సెస్ అయితే OK (200) పంపుతాం
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}