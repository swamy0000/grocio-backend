package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "addresses")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "title", length = 50) // ఉదా: Home, Work, Other
    private String title;
    
    @Column(name = "full_address", columnDefinition = "TEXT")
    private String fullAddress;
    
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
    
    private String landmark;
    
    @Column(name = "receiver_name")
    private String receiverName;
    
    @Column(name = "receiver_phone")
    private String receiverPhone;
    
    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "pincode", length = 20)
    private String pincode;
}