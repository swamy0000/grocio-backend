package com.grocio.backend.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grocio.backend.coupon.entity.Coupon;
import com.grocio.backend.coupon.enums.CouponStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // కేవలం ACTIVE స్టేటస్ లో ఉన్న కూపన్లను మాత్రమే కోడ్ ద్వారా వెతుకుతుంది
    Optional<Coupon> findByCodeAndStatus(String code, CouponStatus status);

    List<Coupon> findByStatus(CouponStatus status);
}