package com.grocio.backend.checkout.validator;

import com.grocio.backend.checkout.dto.CheckoutRequest;
import com.grocio.backend.checkout.exception.CheckoutException;
import org.springframework.stereotype.Component;

@Component
public class CheckoutValidator {

    public void validate(CheckoutRequest request) {
        if (request == null) {
            throw new CheckoutException("Checkout request must not be null");
        }

        if (request.getUserId() == null) {
            throw new CheckoutException("User ID is required for checkout");
        }

        if (request.getAddressId() == null) {
            throw new CheckoutException("Address ID is required for checkout");
        }

        if (request.getPaymentMethod() == null) {
            throw new CheckoutException("Payment method is required for checkout");
        }

        // TODO Validate user
        // TODO Validate address ownership
        // TODO Validate cart
        // TODO Validate inventory
        // TODO Validate coupon
        // TODO Validate payment eligibility
    }
}
