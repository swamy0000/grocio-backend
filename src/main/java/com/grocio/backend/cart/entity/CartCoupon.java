package com.grocio.backend.cart.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_coupons")
public class CartCoupon {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "coupon_code", nullable = false, length = 50)
    private String couponCode;
}
