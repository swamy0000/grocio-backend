package com.grocio.backend.order.customer.mapper;

import com.grocio.backend.address.dto.AddressResponse;
import com.grocio.backend.address.mapper.AddressMapper;
import com.grocio.backend.order.customer.dto.OrderDetailsResponse;
import com.grocio.backend.order.customer.dto.OrderItemResponse;
import com.grocio.backend.order.customer.dto.OrderSummaryResponse;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.entity.OrderItem;
import com.grocio.backend.address.entity.Address;

import java.util.List;

public class CustomerOrderMapper {

    private CustomerOrderMapper() {
    }

    public static OrderSummaryResponse toSummary(Order order) {
        return OrderSummaryResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getOrderTime())
                .build();
    }

    public static OrderDetailsResponse toDetails(Order order, Address deliveryAddress) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(CustomerOrderMapper::toItemResponse)
                .toList();

        AddressResponse addressResponse = deliveryAddress != null
                ? AddressMapper.toResponse(deliveryAddress)
                : null;

        return OrderDetailsResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .deliveryAddress(addressResponse)
                .totalAmount(order.getTotalAmount())
                .items(items)
                .createdAt(order.getOrderTime())
                .build();
    }

    public static OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPriceAtThatTime())
                .build();
    }
}
