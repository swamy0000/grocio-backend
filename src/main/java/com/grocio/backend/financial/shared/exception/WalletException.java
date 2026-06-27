package com.grocio.backend.financial.shared.exception;

/**
 * Exception class for Wallet-related errors in the Financial Engine.
 *
 * Thrown when wallet operations fail or encounter errors.
 */
public class WalletException extends FinancialException {

    /**
     * Constructs a WalletException with the specified message.
     *
     * @param message the detail message
     */
    public WalletException(String message) {
        super(message);
    }

    /**
     * Constructs a WalletException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public WalletException(String message, Throwable cause) {
        super(message, cause);
    }
}
