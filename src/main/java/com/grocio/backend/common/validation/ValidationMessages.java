package com.grocio.backend.common.validation;

public final class ValidationMessages {

    private ValidationMessages() {
    }

    // User

    public static final String PHONE_REQUIRED =
            "Phone number is required.";

    public static final String PIN_REQUIRED =
            "PIN is required.";

    public static final String NAME_REQUIRED =
            "Name is required.";

    public static final String EMAIL_INVALID =
            "Invalid email address.";

    public static final String USER_NOT_FOUND =
            "User not found.";

    public static final String PHONE_ALREADY_EXISTS =
            "Phone number already registered.";

    // Authentication

    public static final String INVALID_CREDENTIALS =
            "Invalid phone number or PIN.";

    public static final String INVALID_TOKEN =
            "Invalid authentication token.";

    // Orders

    public static final String ORDER_NOT_FOUND =
            "Order not found.";

    // Coupons

    public static final String INVALID_COUPON =
            "Invalid or expired coupon.";

    // Wallet

    public static final String INSUFFICIENT_BALANCE =
            "Insufficient wallet balance.";

}