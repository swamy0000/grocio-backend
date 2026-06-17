package com.grocio.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private Long deliveryAddressId; // మీ orders టేబుల్ లోని delivery_address_id కాలమ్ కోసం
    
    // 🟢 కొత్తగా యాడ్ చేసినవి: డిస్కౌంట్స్ మరియు అసలు ధర ట్రాక్ చేయడానికి
    private Double itemTotal;
    private Double couponDiscount;
    
    private Double totalAmount;
    private String paymentMethod;
    private Double deliveryFee;
    private Double handlingCharge;
    private List<OrderItemRequestDTO> items;
    private Double latitude;
    private Double longitude;
}