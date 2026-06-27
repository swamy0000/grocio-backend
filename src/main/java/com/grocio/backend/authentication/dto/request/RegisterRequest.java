package com.grocio.backend.authentication.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String phoneNumber;
    private String pin;
    private String email;
    private String profileImageUrl;
}
