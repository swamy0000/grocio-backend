package com.grocio.backend.authentication.dto;

import lombok.Data;

@Data
public class CheckPhoneRequest {
    private String phoneNumber;
}
