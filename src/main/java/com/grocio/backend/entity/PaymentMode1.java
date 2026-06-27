package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payment_modes")
public class PaymentMode1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // ఉదా: WALLET, COD, GOOGLE_PAY

    @Column(nullable = false)
    private String name; // ఉదా: In-App Wallet, Cash on Delivery

    @Column(nullable = false)
    private String icon; // యాప్ లో చూపించాల్సిన ఐకాన్ లేదా ఇమేజ్ URL

    @Column(name = "display_order")
    private Integer displayOrder; // ఏది ఫస్ట్ కనిపించాలి

    @Column(nullable = false)
    private Boolean enabled = false;

    @Column(name = "coming_soon", nullable = false)
    private Boolean comingSoon = false;
}