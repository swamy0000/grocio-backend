
package com.grocio.backend.financial.payment.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayVerificationResponse {

    private boolean verified;

    private String gatewayPaymentId;

    private String message;

}
