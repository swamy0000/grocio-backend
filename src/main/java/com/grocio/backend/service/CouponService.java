package com.grocio.backend.service;

import com.grocio.backend.entity.Coupon;
import com.grocio.backend.entity.CouponStatus;
import com.grocio.backend.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponValidator couponValidator;

    @Autowired
    private CouponCalculator couponCalculator;
    
    // 💡 గమనిక: మీ ప్రాజెక్ట్ లో ఉన్న రియల్ OrderRepository ని ఇక్కడ ఇంజెక్ట్ చేయండి
    // @Autowired
    // private OrderRepository orderRepository;

    public CouponValidationResult processCoupon(String code, Long userId, Double cartTotal) {
        
        // 1. డేటాబేస్ నుండి కూపన్ ని వెతకడం
        Optional<Coupon> couponOpt = couponRepository.findByCodeAndStatus(code.trim().toUpperCase(), CouponStatus.ACTIVE);
        if (couponOpt.isEmpty()) {
            return CouponValidationResult.failure("Invalid or Expired coupon code");
        }

        Coupon coupon = couponOpt.get();

        try {
            // 2. యూజర్ పాత సక్సెస్ ఫుల్ ఆర్డర్స్ కౌంట్ ని తీసుకోవడం (First Order Only చెక్ కోసం)
            // long pastOrders = orderRepository.countByUserIdAndStatus(userId, "SUCCESS");
            long pastOrders = 0; // ⚡ ప్రస్తుతం హోల్డ్ లో పెట్టాం, మీ ఆర్డర్ టేబుల్ కనెక్ట్ చేసాక పైన లైన్ ఆన్‌లాక్ చేయండి

            // 3. Validator లేయర్ కి పంపి రూల్స్ చెక్ చేయడం
            couponValidator.validate(coupon, userId, cartTotal, pastOrders);

            // 4. Calculator లేయర్ ద్వారా డిస్కౌంట్ అమౌంట్ కనుక్కోవడం
            double discount = couponCalculator.calculateDiscount(coupon, cartTotal);
            double finalAmount = cartTotal - discount;

            return CouponValidationResult.success(coupon.getCode(), discount, finalAmount);

        } catch (IllegalArgumentException e) {
            // Validator విసిరే ఎర్రర్ మెసేజ్ ని క్యాచ్ చేసి ఫెయిల్యూర్ గా పంపుతాం
            return CouponValidationResult.failure(e.getMessage());
        }
    }
}