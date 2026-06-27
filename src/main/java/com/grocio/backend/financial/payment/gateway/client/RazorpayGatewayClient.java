package com.grocio.backend.financial.payment.gateway.client;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.razorpay.Order;
import com.grocio.backend.financial.payment.dto.PaymentWebhookRequest;
import com.grocio.backend.financial.payment.exception.PaymentException;
import com.grocio.backend.financial.payment.gateway.dto.GatewayOrderRequest;
import com.grocio.backend.financial.payment.gateway.dto.GatewayOrderResponse;
import com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationRequest;
import com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationResponse;
import com.razorpay.RazorpayClient;
import com.grocio.backend.financial.payment.gateway.config.RazorpayProperties;
import com.razorpay.Utils;

@Component
public class RazorpayGatewayClient {

    private final RazorpayClient razorpayClient;
    private final RazorpayProperties razorpayProperties;

    public RazorpayGatewayClient(RazorpayClient razorpayClient, RazorpayProperties razorpayProperties) {
        this.razorpayClient = razorpayClient;
        this.razorpayProperties = razorpayProperties;
    }

    /**
     * Prepare a gateway order. SDK/API calls are intentionally not implemented in
     * this sprint.
     */
    public GatewayOrderResponse createOrder(GatewayOrderRequest request) {
        try {
            // Build Razorpay order request JSON
            JSONObject orderReq = new JSONObject();
            // amount is expected to be the smallest currency unit (paise)
            orderReq.put("amount", request.getAmount());
            orderReq.put("currency", request.getCurrency());
            orderReq.put("receipt", request.getReceipt());

            Map<String, Object> notes = new HashMap<>();
            if (request.getNotes() != null) {
                notes.putAll(request.getNotes());
            }
            // Ensure required notes exist as strings
            notes.put("paymentReference", request.getReceipt());
            if (request.getNotes() != null) {
                notes.putIfAbsent("orderId", request.getNotes().getOrDefault("orderId", ""));
                notes.putIfAbsent("userId", request.getNotes().getOrDefault("userId", ""));
            }
            orderReq.put("notes", new JSONObject(notes));

            // Create a Razorpay order using the official SDK.
            Order created = razorpayClient.orders.create(orderReq);

            String id = (String) created.get("id");
            Long amount = ((Number) created.get("amount")).longValue();
            String currency = (String) created.get("currency");
            String status = (String) created.get("status");

            BigDecimal majorAmount = BigDecimal
                    .valueOf(amount)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            GatewayOrderResponse resp = new GatewayOrderResponse();
            resp.setGatewayOrderId(id);
            resp.setGatewayName("RAZORPAY");
            resp.setCurrency(currency);
            resp.setAmount(majorAmount);
            resp.setStatus(status);

            return resp;
        } catch (Exception e) {
            throw new PaymentException("Unable to create payment gateway order. Please try again later.", e);
        }
    }

    /**
     * Verify a payment. No signature verification or external calls in this sprint.
     */
    public GatewayVerificationResponse verifyPayment(GatewayVerificationRequest request) {
        if (request == null
                || request.getGatewayOrderId() == null || request.getGatewayOrderId().isBlank()
                || request.getGatewayPaymentId() == null || request.getGatewayPaymentId().isBlank()
                || request.getGatewaySignature() == null || request.getGatewaySignature().isBlank()) {
            throw new PaymentException("Invalid verification request: missing required fields");
        }

        try {
            org.json.JSONObject payload = new org.json.JSONObject();
            payload.put("razorpay_order_id", request.getGatewayOrderId());
            payload.put("razorpay_payment_id", request.getGatewayPaymentId());
            payload.put("razorpay_signature", request.getGatewaySignature());

            boolean verified = Utils.verifyPaymentSignature(payload, razorpayProperties.getKeySecret());

            GatewayVerificationResponse resp = new GatewayVerificationResponse();
            resp.setVerified(verified);
            resp.setGatewayPaymentId(request.getGatewayPaymentId());
            resp.setMessage(verified ? "verified" : "signature-mismatch");
            return resp;
        } catch (Exception e) {
            throw new PaymentException("Unable to verify payment signature. Please try again later.", e);
        }
    }

    public boolean verifyWebhookSignature(String payload, String signature) {
        if (payload == null || payload.isBlank() || signature == null || signature.isBlank()) {
            throw new PaymentException("Invalid webhook verification request: missing payload or signature");
        }

        try {
            String expectedSignature = calculateHmacSha256(payload, razorpayProperties.getKeySecret());
            return expectedSignature.equalsIgnoreCase(signature.trim());
        } catch (Exception e) {
            throw new PaymentException("Unable to verify webhook signature. Please try again later.", e);
        }
    }

    public PaymentWebhookRequest parseWebhookPayload(String payload) {
        if (payload == null || payload.isBlank()) {
            throw new PaymentException("Webhook payload must not be blank");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(payload, PaymentWebhookRequest.class);
        } catch (JsonProcessingException e) {
            throw new PaymentException("Unable to parse webhook payload", e);
        }
    }

    private String calculateHmacSha256(String payload, String secret)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hashBytes = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder(hashBytes.length * 2);
        for (byte b : hashBytes) {
            hex.append(String.format("%02x", b & 0xff));
        }
        return hex.toString();
    }

}
