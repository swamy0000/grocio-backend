package com.grocio.backend.coupon.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "coupon_usage")
public class CouponUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private Long usageId;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id", nullable = false)
    private Long orderId; // ⚡ ఆర్డర్ సక్సెస్ అయ్యాకే ఇక్కడ రికార్డ్ పడుతుంది

    @Column(nullable = false)
    private Double discount; // ఎంత డిస్కౌంట్ ఇచ్చాం

    @Column(name = "used_at")
    private LocalDateTime usedAt = LocalDateTime.now();
}