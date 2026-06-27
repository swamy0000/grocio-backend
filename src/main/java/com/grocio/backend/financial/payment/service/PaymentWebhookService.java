package com.grocio.backend.financial.payment.service;

import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.payment.exception.PaymentException;
import com.grocio.backend.financial.payment.repository.PaymentRepository;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final PaymentRepository paymentRepository;

    public Optional<Payment> findByGatewayOrderOrPaymentId(String gatewayOrderId, String gatewayPaymentId) {
        if (gatewayOrderId != null && !gatewayOrderId.isBlank()) {
            Optional<Payment> payment = paymentRepository.findByGatewayOrderId(gatewayOrderId);
            if (payment.isPresent()) {
                return payment;
            }
        }
        if (gatewayPaymentId != null && !gatewayPaymentId.isBlank()) {
            return paymentRepository.findByGatewayPaymentId(gatewayPaymentId);
        }
        return Optional.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    public void completeGatewayPayment(Payment payment, String gatewayPaymentId) {
        if (payment == null) {
            throw new PaymentException("Payment must not be null when completing webhook processing");
        }
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(gatewayPaymentId);
        payment.setCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }
}
