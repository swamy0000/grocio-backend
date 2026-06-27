package com.grocio.backend.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartSummary {

    private Double subtotal;
    private Double productDiscount;
    private Double couponDiscount;
    private Double deliveryCharge;
    private Double tax;
    private Double platformFee;
    private Double grandTotal;
}