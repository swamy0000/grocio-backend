package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String name;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    private String pin;

    @Column(name = "wallet_balance")
    private BigDecimal walletBalance;

    @Column(name = "streak_count")
    private Integer streakCount;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

}