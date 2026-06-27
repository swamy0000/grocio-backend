package com.grocio.backend.checkout.dto;

import com.grocio.backend.financial.shared.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {

    private String orderNumber;
    private String paymentReference;
    private BigDecimal payableAmount;
    private PaymentStatus paymentStatus;
    private String gatewayOrderId;
    private String paymentGateway;
    private String currency;
    private String razorpayKeyId;
}
