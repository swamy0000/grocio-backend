package com.grocio.backend.financial.payment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentWebhookRequest {

    private String event;
    private PaymentWebhookPayload payload;
}
