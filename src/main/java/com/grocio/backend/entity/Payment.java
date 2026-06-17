package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "payment_mode", nullable = false)
    private String paymentMode; // ఉదా: WALLET, COD

    @Column(nullable = false)
    private Double amount;

    // PENDING, PAID, FAILED, REFUNDED
    @Column(nullable = false)
    private String status = "PENDING"; 

    @Column(name = "gateway_transaction_id")
    private String gatewayTransactionId; // రేపు ఆన్‌లైన్ పేమెంట్స్ కోసం

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}