package com.grocio.backend.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
    private Long deliveryAddressId;
    private Double itemTotal;
    private Double couponDiscount;
    private Double totalAmount;
    private String paymentMethod;
    private Double deliveryFee;
    private Double handlingCharge;
    private List<OrderItemRequest> items;
    private Double latitude;
    private Double longitude;
}
