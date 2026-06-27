package com.grocio.backend.coupon.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.grocio.backend.coupon.enums.CouponStatus;
import com.grocio.backend.coupon.enums.CouponType;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(nullable = false, unique = true, length = 50)
    private String code; // ఉదా: WELCOME50, GROCIO100

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type", nullable = false)
    private CouponType couponType;

    @Column(name = "discount_type", nullable = false, length = 20)
    private String discountType; // "FLAT" లేదా "PERCENT"

    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Column(name = "min_cart_value")
    private Double minCartValue;

    @Column(name = "max_discount")
    private Double maxDiscount;

    // 🟢 1. Global Usage Limits
    @Column(name = "total_limit")
    private Integer totalLimit; // గరిష్టంగా ఎంతమంది వాడవచ్చు (ఉదా: 5000)

    @Column(name = "used_count")
    private Integer usedCount = 0; // ఇప్పటివరకు గ్లోబల్ గా వాడిన సంఖ్య

    // 🟢 2. Per-User Usage Limits
    @Column(name = "max_use_per_user")
    private Integer maxUsePerUser = 1; // ఒక యూజర్ ఎన్నిసార్లు వాడొచ్చు (డీఫాల్ట్: 1)

    // 🟢 5. Validity Dates
    @Column(name = "valid_from")
    private LocalDateTime validFrom; // కూపన్ ఎప్పటినుండి స్టార్ట్ అవ్వాలి

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate; // కూపన్ ఎప్పుడు ఎక్స్‌పైర్ అవ్వాలి

    // 🟢 4. Coupon Status System
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status = CouponStatus.DRAFT;

    // 🟢 8. Blinkit Style Filters
    @Column(name = "is_first_order_only")
    private Boolean isFirstOrderOnly = false;

    // 🟢 11 & 12. Auto Apply & Priority Logic
    @Column(name = "priority")
    private Integer priority = 0; // ఏ కూపన్ ముందు ఆటో-అప్లై అవ్వాలి

    @Column(name = "auto_apply")
    private Boolean autoApply = false;

    // 🟢 16. Audit Trails
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}