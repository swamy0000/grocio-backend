package com.grocio.backend.repository;

import com.grocio.backend.entity.CartCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartCouponRepository extends JpaRepository<CartCoupon, Long> {
}