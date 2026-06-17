package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wallet_transactions")
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id")
    private Long orderId; // ఆర్డర్ వల్ల కట్ అయితే ఆర్డర్ ఐడీ ఇక్కడ ఉంటుంది

    // CREDIT (Add money/Refund), DEBIT (Order Payment)
    @Column(nullable = false)
    private String type; 

    @Column(nullable = false)
    private Double amount;

    @Column(name = "balance_before", nullable = false)
    private Double balanceBefore;

    @Column(name = "balance_after", nullable = false)
    private Double balanceAfter;

    private String description; // ఉదా: "Paid for Order #1024" లేదా "Refund for Cancelled Order"

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}