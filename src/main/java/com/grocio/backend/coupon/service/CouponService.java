package com.grocio.backend.coupon.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.grocio.backend.cart.repository.CartCouponRepository;
import com.grocio.backend.coupon.dto.BestCouponResponse;
import com.grocio.backend.coupon.dto.CouponAvailableResponse;
import com.grocio.backend.coupon.dto.CouponValidationResult;
import com.grocio.backend.coupon.entity.Coupon;
import com.grocio.backend.coupon.entity.CouponUsage;
import com.grocio.backend.coupon.enums.CouponStatus;
import com.grocio.backend.coupon.repository.CouponRepository;
import com.grocio.backend.coupon.repository.CouponUsageRepository;
import com.grocio.backend.coupon.validator.CouponValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponValidator couponValidator;
    private final CouponCalculator couponCalculator;
    private final CartCouponRepository cartCouponRepository;
    private final CouponUsageRepository couponUsageRepository;

    // 💡 గమనిక: మీ ప్రాజెక్ట్ లో ఉన్న రియల్ OrderRepository ని ఇక్కడ ఇంజెక్ట్
    // చేయండి
    // private final OrderRepository orderRepository;

    public CouponValidationResult processCoupon(String code, Long userId, Double cartTotal) {

        // 1. డేటాబేస్ నుండి కూపన్ ని వెతకడం
        Optional<Coupon> couponOpt = couponRepository.findByCodeAndStatus(code.trim().toUpperCase(),
                CouponStatus.ACTIVE);
        if (couponOpt.isEmpty()) {
            return CouponValidationResult.failure("Invalid or Expired coupon code");
        }

        Coupon coupon = couponOpt.get();

        try {
            // 2. యూజర్ పాత సక్సెస్ ఫుల్ ఆర్డర్స్ కౌంట్ ని తీసుకోవడం (First Order Only చెక్
            // కోసం)
            // long pastOrders = orderRepository.countByUserIdAndStatus(userId, "SUCCESS");
            long pastOrders = 0; // ⚡ ప్రస్తుతం హోల్డ్ లో పెట్టాం, మీ ఆర్డర్ టేబుల్ కనెక్ట్ చేసాక పైన లైన్
                                 // ఆన్‌లాక్ చేయండి

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

    public void consumeCartCouponAfterPayment(Long userId, Long orderId) {
        if (userId == null || orderId == null) {
            return;
        }

        cartCouponRepository.findById(userId).ifPresent(cartCoupon -> {
            cartCouponRepository.delete(cartCoupon);
            String couponCode = cartCoupon.getCouponCode();
            if (couponCode != null && !couponCode.isBlank()) {
                Optional<Coupon> couponOpt = couponRepository.findByCodeAndStatus(couponCode.trim().toUpperCase(),
                        CouponStatus.ACTIVE);
                couponOpt.ifPresent(coupon -> {
                    CouponUsage usage = new CouponUsage();
                    usage.setCouponId(coupon.getCouponId());
                    usage.setUserId(userId);
                    usage.setOrderId(orderId);
                    usage.setUsedAt(LocalDateTime.now());
                    couponUsageRepository.save(usage);
                });
            }
        });
    }

    public List<CouponAvailableResponse> getAvailableCoupons(Long userId, Double cartTotal) {

        List<Coupon> coupons = couponRepository.findByStatus(CouponStatus.ACTIVE);

        List<CouponAvailableResponse> response = new ArrayList<>();

        for (Coupon coupon : coupons) {

            boolean applicable = true;
            String reason = null;

            try {

                long pastOrders = 0;

                couponValidator.validate(
                        coupon,
                        userId,
                        cartTotal,
                        pastOrders);

            } catch (IllegalArgumentException ex) {
                applicable = false;
                reason = ex.getMessage();
            }

            response.add(new CouponAvailableResponse(
                    coupon.getCode(),
                    "₹" + coupon.getDiscountValue().intValue() + " OFF",
                    coupon.getMinCartValue() == null ? "" : "On orders above ₹" + coupon.getMinCartValue().intValue(),
                    coupon.getDiscountType(),
                    coupon.getDiscountValue(),
                    coupon.getMinCartValue(),
                    applicable,
                    reason));
        }

        return response;
    }

    public BestCouponResponse getBestCoupon(Long userId, Double cartTotal) {

        List<CouponAvailableResponse> coupons = getAvailableCoupons(userId, cartTotal);

        CouponAvailableResponse best = null;

        for (CouponAvailableResponse c : coupons) {

            if (!c.isApplicable())
                continue;

            if (best == null || c.getDiscountValue() > best.getDiscountValue()) {
                best = c;
            }
        }

        if (best == null) {
            return new BestCouponResponse(false, null, 0.0);
        }

        return new BestCouponResponse(
                true,
                best.getCode(),
                best.getDiscountValue());
    }

}