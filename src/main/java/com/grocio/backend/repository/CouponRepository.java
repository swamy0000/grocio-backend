package com.grocio.backend.repository;

import com.grocio.backend.entity.Coupon;
import com.grocio.backend.entity.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // కేవలం ACTIVE స్టేటస్ లో ఉన్న కూపన్లను మాత్రమే కోడ్ ద్వారా వెతుకుతుంది
    Optional<Coupon> findByCodeAndStatus(String code, CouponStatus status);
}