package com.grocio.backend.financial.payment.factory;

import org.springframework.stereotype.Component;

import com.grocio.backend.financial.payment.exception.PaymentException;
import com.grocio.backend.financial.payment.gateway.PaymentProcessor;
import com.grocio.backend.financial.payment.gateway.RazorpayProcessor;
import com.grocio.backend.financial.shared.enums.PaymentGateway;

@Component
public class PaymentProcessorFactory {

    private final RazorpayProcessor razorpayProcessor;

    public PaymentProcessorFactory(RazorpayProcessor razorpayProcessor) {
        this.razorpayProcessor = razorpayProcessor;
    }

 public PaymentProcessor getProcessor(PaymentGateway gateway) {

    if (gateway == null) {
        throw new PaymentException("Payment gateway is required");
    }

    return switch (gateway) {

        case RAZORPAY -> razorpayProcessor;

        case NONE ->
                throw new PaymentException("No payment gateway configured");

        default ->
                throw new PaymentException("Unsupported payment gateway");
    };
}



}
