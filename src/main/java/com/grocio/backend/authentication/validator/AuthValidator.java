package com.grocio.backend.authentication.validator;

import com.grocio.backend.authentication.dto.request.CheckPhoneRequest;
import com.grocio.backend.authentication.dto.request.LoginRequest;
import com.grocio.backend.authentication.dto.request.RegisterRequest;
import com.grocio.backend.authentication.dto.request.ResetPinRequest;
import com.grocio.backend.common.exception.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AuthValidator {

    public void validateCheckPhone(CheckPhoneRequest request) {
        if (request == null || !StringUtils.hasText(request.getPhoneNumber())) {
            throw new ValidationException("Phone number is required.");
        }
        validatePhoneNumber(request.getPhoneNumber());
    }

    public void validateRegister(RegisterRequest request) {
        if (request == null) {
            throw new ValidationException("Register request cannot be empty.");
        }
        if (!StringUtils.hasText(request.getName())) {
            throw new ValidationException("Name is required.");
        }
        validatePhoneNumber(request.getPhoneNumber());
        validatePin(request.getPin());
    }

    public void validateLogin(LoginRequest request) {
        if (request == null) {
            throw new ValidationException("Login request cannot be empty.");
        }
        validatePhoneNumber(request.getPhoneNumber());
        validatePin(request.getPin());
    }

    public void validateResetPin(ResetPinRequest request) {
        if (request == null) {
            throw new ValidationException("Reset PIN request cannot be empty.");
        }
        validatePhoneNumber(request.getPhoneNumber());
        validatePin(request.getPin());
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new ValidationException("Phone number is required.");
        }
        if (!phoneNumber.matches("\\d{10}")) {
            throw new ValidationException("Phone number must be 10 digits.");
        }
    }

    private void validatePin(String pin) {
        if (!StringUtils.hasText(pin)) {
            throw new ValidationException("PIN is required.");
        }
        if (!pin.matches("\\d{4,6}")) {
            throw new ValidationException("PIN must be 4 to 6 digits.");
        }
    }
}
