package com.grocio.backend.financial.payment.gateway;

import org.springframework.stereotype.Component;

import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.payment.gateway.client.RazorpayGatewayClient;
import com.grocio.backend.financial.payment.gateway.dto.GatewayOrderRequest;
import com.grocio.backend.financial.payment.gateway.dto.GatewayOrderResponse;
import com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationRequest;
import com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationResponse;

@Component
public class RazorpayProcessor implements PaymentProcessor {

    private final RazorpayGatewayClient client;

    public RazorpayProcessor(RazorpayGatewayClient client) {
        this.client = client;
    }

    @Override
    public GatewayOrderResponse createGatewayOrder(Payment payment, GatewayOrderRequest request) {
        // Delegate SDK/API concerns to the gateway client. No external calls are
        // performed by the client in this sprint.
        return client.createOrder(request);
    }

    @Override
    public GatewayVerificationResponse verifyPayment(GatewayVerificationRequest request) {
        // Delegate verification to the gateway client.
        return client.verifyPayment(request);
    }
}