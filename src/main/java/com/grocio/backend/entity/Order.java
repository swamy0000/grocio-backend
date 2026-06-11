package com.grocio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    // 🟢 మీ ఇమేజ్ లో ఉన్నట్లుగా కరెక్ట్ కాలమ్ నేమ్ మ్యాప్ చేశాను
    @Column(name = "delivery_address_id", nullable = false)
    private Long deliveryAddressId;
    
    @Column(name = "delivery_partner_id")
    private Long deliveryPartnerId; 
    
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;
    
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
    
    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Column(nullable = false)
    private String status; 
    
    // 🟢 మీ ఇమేజ్ లో ఉన్నట్లుగా 'order_time' కాలమ్ మ్యాప్ చేశాను
    @Column(name = "order_time", updatable = false)
    private LocalDateTime orderTime = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Column(name = "delivery_fee")
    private Double deliveryFee;
    
    @Column(name = "handling_charge")
    private Double handlingCharge;
    
    @Column(name = "delivery_otp", nullable = false, length = 4)
    private String deliveryOtp;
    
    @Column(name = "delivery_latitude")
    private Double deliveryLatitude;
    
    @Column(name = "delivery_longitude")
    private Double deliveryLongitude;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    
}