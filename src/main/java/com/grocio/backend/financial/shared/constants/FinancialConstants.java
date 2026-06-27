package com.grocio.backend.financial.shared.constants;

/**
 * Central constants class for the Financial Engine.
 *
 * Contains default business configuration values for:
 * - Payment
 * - Wallet
 * - Refund
 *
 * This class is not meant to be instantiated.
 */
public final class FinancialConstants {

    public static final String MODULE_NAME = "financial";

    // Currency Configuration
    public static final String DEFAULT_CURRENCY = "INR";

    // Payment Configuration
    public static final Integer PAYMENT_TIMEOUT_MINUTES = 10;

    public static final Integer MAX_PAYMENT_RETRIES = 3;

    public static final String DEFAULT_PAYMENT_GATEWAY = "RAZORPAY";

    // Refund Configuration
    public static final Integer MAX_REFUND_RETRIES = 3;

    private FinancialConstants() {
        throw new UnsupportedOperationException("FinancialConstants is a utility class and cannot be instantiated.");
    }
}
