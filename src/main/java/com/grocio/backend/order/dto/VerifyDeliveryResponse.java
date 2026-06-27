package com.grocio.backend.order.dto;

import lombok.Data;

@Data
public class VerifyDeliveryResponse {
    private boolean success;
    private String message;

    public static VerifyDeliveryResponse success(String message) {
        VerifyDeliveryResponse response = new VerifyDeliveryResponse();
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }

    public static VerifyDeliveryResponse failure(String message) {
        VerifyDeliveryResponse response = new VerifyDeliveryResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}