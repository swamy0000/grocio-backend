package com.grocio.backend.repository;

import com.grocio.backend.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {
    // ప్రైవేట్ కూపన్ ఈ యూజర్‌కి అసైన్ అయ్యిందో లేదో చెక్ చేస్తుంది
    boolean existsByCouponIdAndUserId(Long couponId, Long userId);
}