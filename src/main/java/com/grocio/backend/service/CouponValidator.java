package com.grocio.backend.service;

import com.grocio.backend.entity.Coupon;
import com.grocio.backend.entity.CouponStatus;
import com.grocio.backend.entity.CouponType;
import com.grocio.backend.repository.CouponUsageRepository;
import com.grocio.backend.repository.CouponUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CouponValidator {

    @Autowired
    private CouponUserRepository couponUserRepository;

    @Autowired
    private CouponUsageRepository couponUsageRepository;

    // ప్రొడక్షన్ రూల్: హ్యాకర్లకి లూప్‌హోల్స్ ఇవ్వకుండా ఒకే స్టాండర్డ్ ఎర్రర్ మెసేజ్ వాడటం
    private static final String INVALID_OR_EXPIRED = "Invalid or Expired coupon code";

    public void validate(Coupon coupon, Long userId, Double cartTotal, long userPastOrderCount) {
        
        // 1. Status Check (ACTIVE కాకపోతే ఇన్వాలిడ్)
        if (coupon.getStatus() != CouponStatus.ACTIVE) {
            throw new IllegalArgumentException(INVALID_OR_EXPIRED);
        }

        // 2. Date Validation (Valid From & Expiry Date చెక్)
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidFrom() != null && now.isBefore(coupon.getValidFrom())) {
            throw new IllegalArgumentException(INVALID_OR_EXPIRED);
        }
        if (coupon.getExpiryDate() != null && now.isAfter(coupon.getExpiryDate())) {
            throw new IllegalArgumentException(INVALID_OR_EXPIRED);
        }

        // 3. Global Total Limit Check (ఉదా: మాక్స్ 5000 మందికే)
        if (coupon.getTotalLimit() != null && coupon.getUsedCount() >= coupon.getTotalLimit()) {
            throw new IllegalArgumentException(INVALID_OR_EXPIRED);
        }

        // 4. Coupon Ownership Mapping (Many-to-Many Private Coupons Logic)
        if (coupon.getCouponType() == CouponType.PRIVATE) {
            boolean isAssigned = couponUserRepository.existsByCouponIdAndUserId(coupon.getCouponId(), userId);
            if (!isAssigned) {
                throw new IllegalArgumentException(INVALID_OR_EXPIRED);
            }
        }

        // 5. Per-User Usage Limit Check (ఒక యూజర్ మాక్స్ ఎన్నిసార్లు వాడొచ్చు)
        long timesUsedByUser = couponUsageRepository.countByCouponIdAndUserId(coupon.getCouponId(), userId);
        if (coupon.getMaxUsePerUser() != null && timesUsedByUser >= coupon.getMaxUsePerUser()) {
            throw new IllegalArgumentException(INVALID_OR_EXPIRED);
        }

        // 6. Blinkit Style: First Order Only Restriction
        if (coupon.getIsFirstOrderOnly() != null && coupon.getIsFirstOrderOnly()) {
            if (userPastOrderCount > 0) {
                throw new IllegalArgumentException(INVALID_OR_EXPIRED);
            }
        }

        // 7. Minimum Cart Value Check (ఇది బిజినెస్ మైల్‌స్టోన్ కాబట్టి కంటిన్యూ అవ్వడానికి యూజర్‌కి గైడ్ చేస్తాం)
        if (coupon.getMinCartValue() != null && cartTotal < coupon.getMinCartValue()) {
            double remaining = coupon.getMinCartValue() - cartTotal;
            throw new IllegalArgumentException("Add ₹" + String.format("%.0f", remaining) + " more items to unlock this coupon!");
        }
    }
}