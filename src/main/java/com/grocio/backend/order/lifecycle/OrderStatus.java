package com.grocio.backend.order.lifecycle;

import java.util.Locale;

public enum OrderStatus {
    PENDING_PAYMENT,
    CONFIRMED,
    PACKED,
    SHIPPED,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    public static OrderStatus from(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Order status must not be null or blank");
        }

        String normalized = status.trim().toUpperCase(Locale.ROOT);
        if ("PENDING".equals(normalized)) {
            return PENDING_PAYMENT;
        }

        if ("PLACED".equals(normalized)) {
            return CONFIRMED;
        }

        try {
            return OrderStatus.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported order status: " + status, ex);
        }
    }
}
