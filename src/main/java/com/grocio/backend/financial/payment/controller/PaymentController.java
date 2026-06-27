package com.grocio.backend.financial.payment.controller;

import com.grocio.backend.financial.payment.dto.PaymentRequest;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.financial.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        URI location = URI.create("/api/payments/" + response.getPaymentReference());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{paymentReference}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentReference) {
        return ResponseEntity.ok(paymentService.getPayment(paymentReference));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByUser(userId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrder(orderId));
    }

    @PostMapping("/verify")
    public ResponseEntity<com.grocio.backend.financial.payment.dto.PaymentVerificationResponse> verifyPayment(
            @RequestBody com.grocio.backend.financial.payment.dto.PaymentVerificationRequest request) {
        com.grocio.backend.financial.payment.dto.PaymentVerificationResponse resp = paymentService
                .verifyPayment(request);
        return ResponseEntity.ok(resp);
    }
}
