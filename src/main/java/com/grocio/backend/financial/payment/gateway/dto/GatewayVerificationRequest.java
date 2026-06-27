
package com.grocio.backend.financial.payment.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayVerificationRequest {

    private String gatewayOrderId;

    private String gatewayPaymentId;

    private String gatewaySignature;

}