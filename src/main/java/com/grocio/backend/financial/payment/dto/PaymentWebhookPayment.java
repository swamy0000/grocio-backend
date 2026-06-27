package com.grocio.backend.financial.payment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentWebhookPayment {

    private PaymentWebhookEntity entity;
}
