package com.grocio.backend.authentication.dto.request;

import lombok.Data;

@Data
public class ResetPinRequest {
    private String phoneNumber;
    private String pin;
}
