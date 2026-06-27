package com.grocio.backend.financial.payment.mapper;

import com.grocio.backend.financial.payment.dto.PaymentRequest;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.shared.enums.PaymentGateway;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import com.grocio.backend.order.entity.Order;

public class PaymentMapper {

    private PaymentMapper() {
    }

    public static Payment toEntity(PaymentRequest request, Order order) {
        if (request == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setCurrency("INR");
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(PaymentStatus.CREATED);
        payment.setPaymentGateway(PaymentGateway.NONE);
        return payment;
    }

    public static PaymentResponse toResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentResponse response = new PaymentResponse();
        response.setPaymentReference(payment.getPaymentReference());
        response.setOrderId(payment.getOrder() != null ? payment.getOrder().getOrderId() : null);
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setPaymentGateway(payment.getPaymentGateway());
        response.setStatus(payment.getStatus());
        response.setGatewayOrderId(payment.getGatewayOrderId());
        response.setGatewayPaymentId(payment.getGatewayPaymentId());
        response.setCompletedAt(payment.getCompletedAt());
        return response;
    }
}
