package com.grocio.backend.order.dto;

import lombok.Data;

@Data
public class OrderPlacementResponse {
    private boolean success;
    private String message;
    private Long orderId;

    public static OrderPlacementResponse success(Long orderId, String message) {
        OrderPlacementResponse response = new OrderPlacementResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setOrderId(orderId);
        return response;
    }

    public static OrderPlacementResponse failure(String message) {
        OrderPlacementResponse response = new OrderPlacementResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}