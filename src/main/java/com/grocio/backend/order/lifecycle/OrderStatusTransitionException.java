package com.grocio.backend.order.lifecycle;

public class OrderStatusTransitionException extends RuntimeException {

    public OrderStatusTransitionException(String message) {
        super(message);
    }

    public OrderStatusTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
