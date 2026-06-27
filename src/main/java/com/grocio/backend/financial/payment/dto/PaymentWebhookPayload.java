package com.grocio.backend.financial.payment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentWebhookPayload {

    private PaymentWebhookPayment payment;
}
