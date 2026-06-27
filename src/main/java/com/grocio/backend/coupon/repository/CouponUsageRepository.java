package com.grocio.backend.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grocio.backend.coupon.entity.CouponUsage;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    // ఒక నిర్దిష్ట యూజర్ ఈ కూపన్‌ను ఎన్నిసార్లు వాడాడో కౌంట్ చేస్తుంది
    long countByCouponIdAndUserId(Long couponId, Long userId);
}