package com.grocio.backend.order.dto;

import com.grocio.backend.product.dto.ProductResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private Long id;
    private ProductResponse product;
    private BigDecimal priceAtThatTime;
    private Integer quantity;
}