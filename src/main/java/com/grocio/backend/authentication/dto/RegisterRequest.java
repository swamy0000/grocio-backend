package com.grocio.backend.authentication.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String phoneNumber;
    private String pin;
    private String email;
}
