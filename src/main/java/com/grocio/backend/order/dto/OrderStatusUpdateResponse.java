package com.grocio.backend.order.dto;

import lombok.Data;

@Data
public class OrderStatusUpdateResponse {
    private boolean success;
    private String message;
    private String currentStatus;

    public static OrderStatusUpdateResponse success(String currentStatus, String message) {
        OrderStatusUpdateResponse response = new OrderStatusUpdateResponse();
        response.setSuccess(true);
        response.setCurrentStatus(currentStatus);
        response.setMessage(message);
        return response;
    }

    public static OrderStatusUpdateResponse failure(String message) {
        OrderStatusUpdateResponse response = new OrderStatusUpdateResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}