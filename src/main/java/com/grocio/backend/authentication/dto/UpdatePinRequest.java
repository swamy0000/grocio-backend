package com.grocio.backend.authentication.dto;

import lombok.Data;

@Data
public class UpdatePinRequest {
    private String phoneNumber;
    private String pin;
}
