package com.grocio.backend.financial.payment.service;

import com.grocio.backend.financial.payment.dto.PaymentRequest;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.financial.shared.enums.PaymentMethod;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import com.grocio.backend.order.entity.Order;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse createPaymentForOrder(Order order, PaymentMethod paymentMethod);

    PaymentResponse getPayment(String paymentReference);

    List<PaymentResponse> getPaymentsByUser(Long userId);

    List<PaymentResponse> getPaymentsByOrder(Long orderId);

    PaymentResponse updatePaymentStatus(String paymentReference, PaymentStatus status);

    com.grocio.backend.financial.payment.dto.PaymentVerificationResponse verifyPayment(
            com.grocio.backend.financial.payment.dto.PaymentVerificationRequest request);
}
