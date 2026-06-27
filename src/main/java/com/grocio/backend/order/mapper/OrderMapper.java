package com.grocio.backend.order.mapper;

import com.grocio.backend.order.dto.OrderItemResponse;
import com.grocio.backend.order.dto.OrderResponse;
import com.grocio.backend.product.mapper.ProductMapper;

public class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(com.grocio.backend.order.entity.Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUserId());
        response.setDeliveryAddressId(order.getDeliveryAddressId());
        response.setDeliveryPartnerId(order.getDeliveryPartnerId());
        response.setItemTotal(order.getItemTotal());
        response.setDeliveryFee(order.getDeliveryFee());
        response.setHandlingCharge(order.getHandlingCharge());
        response.setCouponDiscount(order.getCouponDiscount());
        response.setTotalAmount(order.getTotalAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        response.setOrderTime(order.getOrderTime());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setDeliveryOtp(order.getDeliveryOtp());
        response.setDeliveryLatitude(order.getDeliveryLatitude());
        response.setDeliveryLongitude(order.getDeliveryLongitude());
        response.setItems(order.getItems().stream().map(OrderMapper::toItemResponse).toList());
        return response;
    }

    public static OrderItemResponse toItemResponse(com.grocio.backend.order.entity.OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setProduct(ProductMapper.toResponse(item.getProduct()));
        response.setPriceAtThatTime(item.getPriceAtThatTime());
        response.setQuantity(item.getQuantity());
        return response;
    }
}