package com.grocio.backend.financial.payment.gateway;

import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.payment.gateway.dto.GatewayOrderResponse;
import com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationResponse;

public interface PaymentProcessor {

        GatewayOrderResponse createGatewayOrder(
                        Payment payment,
                        com.grocio.backend.financial.payment.gateway.dto.GatewayOrderRequest request);

        GatewayVerificationResponse verifyPayment(
                        com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationRequest request);

}
