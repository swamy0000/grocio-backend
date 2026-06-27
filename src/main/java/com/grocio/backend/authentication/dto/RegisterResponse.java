package com.grocio.backend.authentication.dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private boolean success;
    private String message;
    private Long userId;
}
