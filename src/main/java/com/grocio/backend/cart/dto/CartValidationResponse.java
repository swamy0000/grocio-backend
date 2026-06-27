package com.grocio.backend.cart.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartValidationResponse {
    private Boolean canProceed; // true if checkout can proceed
    private String message;
    private List<String> outOfStockItems;
}
