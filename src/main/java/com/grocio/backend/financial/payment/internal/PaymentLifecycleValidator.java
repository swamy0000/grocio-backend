package com.grocio.backend.financial.payment.internal;

import com.grocio.backend.financial.payment.exception.PaymentException;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentLifecycleValidator {

    public void validateTransition(PaymentStatus currentStatus, PaymentStatus newStatus) {
        boolean allowed = switch (currentStatus) {
            case CREATED -> newStatus == PaymentStatus.INITIATED;
            case INITIATED -> newStatus == PaymentStatus.PENDING;
            case PENDING -> switch (newStatus) {
                case SUCCESS, FAILED, EXPIRED -> true;
                default -> false;
            };
            case SUCCESS -> newStatus == PaymentStatus.REFUNDED;
            default -> false;
        };

        if (!allowed) {
            throw new PaymentException("Invalid payment status transition: " + currentStatus + " -> " + newStatus);
        }
    }
}
