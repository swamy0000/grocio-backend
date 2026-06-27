package com.grocio.backend.authentication.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String phoneNumber;
    private String pin;
}
