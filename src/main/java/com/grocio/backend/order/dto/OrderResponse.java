package com.grocio.backend.order.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long orderId;
    private Long userId;
    private Long deliveryAddressId;
    private Long deliveryPartnerId;
    private Double itemTotal;
    private Double deliveryFee;
    private Double handlingCharge;
    private Double couponDiscount;
    private Double totalAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String status;
    private LocalDateTime orderTime;
    private LocalDateTime updatedAt;
    private String deliveryOtp;
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    private List<OrderItemResponse> items;
}