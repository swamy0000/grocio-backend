package com.grocio.backend.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grocio.backend.coupon.entity.CouponUser;

@Repository
public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {
    // ప్రైవేట్ కూపన్ ఈ యూజర్‌కి అసైన్ అయ్యిందో లేదో చెక్ చేస్తుంది
    boolean existsByCouponIdAndUserId(Long couponId, Long userId);
}