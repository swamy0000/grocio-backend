package com.grocio.backend.financial.shared.exception;

/**
 * Exception class for Payment-related errors in the Financial Engine.
 *
 * Thrown when payment operations fail or encounter errors.
 */
public class PaymentException extends FinancialException {

    /**
     * Constructs a PaymentException with the specified message.
     *
     * @param message the detail message
     */
    public PaymentException(String message) {
        super(message);
    }

    /**
     * Constructs a PaymentException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
