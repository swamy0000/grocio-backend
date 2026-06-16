package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_coupons")
public class CartCoupon {
    @Id
    @Column(name = "user_id")
    private Long userId; // ఒక యూజర్ కార్ట్ కి ఒకే కూపన్ లింక్ అవుతుంది

    @Column(name = "coupon_code", nullable = false, length = 50)
    private String couponCode;
}