package com.grocio.backend.order.util;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * OtpGenerator utility for generating secure delivery OTPs
 */
@Component
public class OtpGenerator {

    private static final int OTP_LENGTH = 4;
    private static final int OTP_MAX_VALUE = 10000;
    private final Random random = new Random();

    /**
     * Generate a 4-digit OTP for delivery verification
     * @return 4-digit OTP as String (e.g., "0123", "9999")
     */
    public String generateOtp() {
        return String.format("%04d", random.nextInt(OTP_MAX_VALUE));
    }
}
