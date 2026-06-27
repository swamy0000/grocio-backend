package com.grocio.backend.order.validator;

import com.grocio.backend.common.exception.ValidationException;
import com.grocio.backend.order.dto.OrderRequest;
import com.grocio.backend.order.dto.OrderItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public void validateOrderRequest(OrderRequest request) {
        if (request == null) {
            throw new ValidationException("Order request cannot be empty.");
        }
        if (request.getUserId() == null) {
            throw new ValidationException("User ID is required.");
        }
        if (request.getDeliveryAddressId() == null) {
            throw new ValidationException("Delivery address is required.");
        }
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ValidationException("Cart is empty! Cannot place order.");
        }
        request.getItems().forEach(this::validateOrderItem);
    }

    private void validateOrderItem(OrderItemRequest item) {
        if (item == null) {
            throw new ValidationException("Order items cannot contain null entries.");
        }
        if (item.getProductId() == null) {
            throw new ValidationException("Product ID is required for every order item.");
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new ValidationException("Order item quantity must be greater than zero.");
        }
    }
}