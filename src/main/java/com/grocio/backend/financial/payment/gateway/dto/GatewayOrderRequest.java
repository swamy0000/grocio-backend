package com.grocio.backend.financial.payment.gateway.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayOrderRequest {
    // amount in the smallest currency unit (e.g., paise)
    private Long amount;
    private String currency;
    private String receipt;
    private Map<String, String> notes;
}
