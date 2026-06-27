package com.grocio.backend.order.customer.service;

import com.grocio.backend.address.entity.Address;
import com.grocio.backend.address.repository.AddressRepository;
import com.grocio.backend.order.customer.dto.OrderDetailsResponse;
import com.grocio.backend.order.customer.dto.OrderSummaryResponse;
import com.grocio.backend.order.customer.mapper.CustomerOrderMapper;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.lifecycle.OrderStatus;
import com.grocio.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getCustomerOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByOrderTimeDesc(userId, pageable)
                .map(CustomerOrderMapper::toSummary);
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getCustomerOrdersByStatus(Long userId, String status, Pageable pageable) {
        OrderStatus targetStatus = OrderStatus.from(status);
        return orderRepository.findByUserIdAndStatusOrderByOrderTimeDesc(userId, targetStatus, pageable)
                .map(CustomerOrderMapper::toSummary);
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getCustomerOrdersByDateRange(Long userId, LocalDateTime dateFrom,
            LocalDateTime dateTo, Pageable pageable) {
        return orderRepository.findByUserIdAndOrderTimeBetweenOrderByOrderTimeDesc(userId, dateFrom, dateTo, pageable)
                .map(CustomerOrderMapper::toSummary);
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getCustomerOrdersWithFilters(Long userId, String status,
            LocalDateTime dateFrom, LocalDateTime dateTo,
            Pageable pageable) {
        OrderStatus targetStatus = OrderStatus.from(status);
        return orderRepository.findByUserIdAndStatusAndOrderTimeBetweenOrderByOrderTimeDesc(
                userId, targetStatus, dateFrom, dateTo, pageable)
                .map(CustomerOrderMapper::toSummary);
    }

    @Transactional(readOnly = true)
    public OrderDetailsResponse getOrderDetails(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        validateOrderOwnership(order, userId);

        Address deliveryAddress = addressRepository.findById(order.getDeliveryAddressId())
                .orElse(null);

        return CustomerOrderMapper.toDetails(order, deliveryAddress);
    }

    private void validateOrderOwnership(Order order, Long userId) {
        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found with ID: " + order.getOrderId());
        }
    }
}
