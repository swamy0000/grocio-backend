package com.grocio.backend.authentication.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private Long userId;
    private String name;
}
