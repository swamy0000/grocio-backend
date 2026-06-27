package com.grocio.backend.checkout.mapper;

import com.grocio.backend.checkout.dto.CheckoutResponse;
import com.grocio.backend.checkout.internal.CheckoutContext;
import com.grocio.backend.financial.payment.gateway.config.RazorpayProperties;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.financial.shared.enums.PaymentMethod;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CheckoutMapper {

    private final RazorpayProperties razorpayProperties;

    public CheckoutResponse toResponse(CheckoutContext context) {
        if (context == null) {
            throw new IllegalArgumentException("Checkout context cannot be null");
        }

        String orderNumber = null;
        if (context.getOrder() != null && context.getOrder().getOrderId() != null) {
            orderNumber = "ORD-" + context.getOrder().getOrderId();
        }

        PaymentResponse payment = context.getPaymentResponse();

        String paymentReference = payment != null
                ? payment.getPaymentReference()
                : null;

        PaymentStatus paymentStatus = payment != null
                ? payment.getStatus()
                : null;

        BigDecimal payableAmount = context.getFinalPayableAmount() != null
                ? context.getFinalPayableAmount()
                : BigDecimal.ZERO;

        CheckoutResponse response = new CheckoutResponse();

        response.setOrderNumber(orderNumber);
        response.setPaymentReference(paymentReference);
        response.setPayableAmount(payableAmount);
        response.setPaymentStatus(paymentStatus);

        if (payment != null) {
            response.setGatewayOrderId(payment.getGatewayOrderId());

            response.setPaymentGateway(
                    payment.getPaymentGateway() != null
                            ? payment.getPaymentGateway().name()
                            : null);

            response.setCurrency(payment.getCurrency());

            // Only expose Razorpay key for Razorpay payments
            if (PaymentMethod.RAZORPAY.equals(payment.getPaymentMethod())) {
                response.setRazorpayKeyId(razorpayProperties.getKeyId());
            }
        }

        return response;
    }
}