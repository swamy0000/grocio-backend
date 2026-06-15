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
    
    // 🟢 కొత్త ఫీల్డ్స్ (మ్యాప్-ఫస్ట్ ఆర్కిటెక్చర్ కోసం)
    @Column(name = "flat_no")
    private String flatNo; // యూజర్ ఎంటర్ చేసే ఫ్లాట్/హౌస్ నంబర్
    
    @Column(name = "formatted_address", columnDefinition = "TEXT")
    private String formattedAddress; // మ్యాప్ నుండి వచ్చే స్ట్రీట్/ఏరియా అడ్రస్
    
    @Column(name = "city", length = 100)
    private String city; // ఆటోమెటిక్
    
    @Column(name = "state", length = 100)
    private String state; // ఆటోమెటిక్
    
    @Column(name = "place_id")
    private String placeId; // OSM/Google ఇచ్చే యూనిక్ ఐడీ (ఫ్యూచర్ కోసం)
    
    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;
    
    private String landmark;
    
    @Column(name = "receiver_name")
    private String receiverName;
    
    @Column(name = "receiver_phone")
    private String receiverPhone;
    
    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "pincode", length = 20)
    private String pincode;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}