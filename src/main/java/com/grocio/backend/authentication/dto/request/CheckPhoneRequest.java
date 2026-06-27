package com.grocio.backend.authentication.dto.request;

import lombok.Data;

@Data
public class CheckPhoneRequest {
    private String phoneNumber;
}
