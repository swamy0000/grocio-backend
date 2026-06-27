package com.grocio.backend.financial.shared.exception;

/**
 * Base exception class for all Financial Engine exceptions.
 *
 * Serves as the root exception for:
 * - Payment
 * - Wallet
 * - Refund
 *
 * All domain-specific financial exceptions should extend this class.
 */
public class FinancialException extends RuntimeException {

    /**
     * Constructs a FinancialException with the specified message.
     *
     * @param message the detail message
     */
    public FinancialException(String message) {
        super(message);
    }

    /**
     * Constructs a FinancialException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public FinancialException(String message, Throwable cause) {
        super(message, cause);
    }
}
