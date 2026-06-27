package com.grocio.backend.financial.shared.exception;

/**
 * Exception class for Refund-related errors in the Financial Engine.
 *
 * Thrown when refund operations fail or encounter errors.
 */
public class RefundException extends FinancialException {

    /**
     * Constructs a RefundException with the specified message.
     *
     * @param message the detail message
     */
    public RefundException(String message) {
        super(message);
    }

    /**
     * Constructs a RefundException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public RefundException(String message, Throwable cause) {
        super(message, cause);
    }
}
