package com.grocio.backend.checkout.dto;

import com.grocio.backend.financial.shared.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    private Long userId;
    private Long addressId;
    private String couponCode;
    private PaymentMethod paymentMethod;
}
