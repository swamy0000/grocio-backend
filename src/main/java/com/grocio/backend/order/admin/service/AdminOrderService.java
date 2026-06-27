package com.grocio.backend.order.admin.service;

import com.grocio.backend.order.admin.dto.OrderStatusUpdateRequest;
import com.grocio.backend.order.admin.dto.OrderStatusUpdateResponse;
import com.grocio.backend.order.admin.mapper.AdminOrderMapper;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.lifecycle.OrderLifecycleService;
import com.grocio.backend.order.lifecycle.OrderStateValidator;
import com.grocio.backend.order.lifecycle.OrderStatus;
import com.grocio.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminOrderService {

    private static final Logger log = LoggerFactory.getLogger(AdminOrderService.class);

    private final OrderLifecycleService lifecycleService;
    private final OrderRepository orderRepository;
    private final OrderStateValidator orderStateValidator;

    @Transactional(rollbackFor = Exception.class)
    public OrderStatusUpdateResponse packOrder(Long orderId, OrderStatusUpdateRequest req) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        OrderStatus previous = order.getStatus();
        orderStateValidator.validate(previous, OrderStatus.PACKED);

        Order updated = lifecycleService.packOrder(orderId);

        log.info("AUDIT order={} prev={} curr={} actor={} time={}", orderId, previous, updated.getStatus(),
                req != null ? req.getActor() : null, LocalDateTime.now());

        return AdminOrderMapper.toResponse(updated, previous, req != null ? req.getRemarks() : null);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderStatusUpdateResponse shipOrder(Long orderId, OrderStatusUpdateRequest req) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        OrderStatus previous = order.getStatus();
        orderStateValidator.validate(previous, OrderStatus.SHIPPED);

        Order updated = lifecycleService.shipOrder(orderId);

        log.info("AUDIT order={} prev={} curr={} actor={} time={}", orderId, previous, updated.getStatus(),
                req != null ? req.getActor() : null, LocalDateTime.now());

        return AdminOrderMapper.toResponse(updated, previous, req != null ? req.getRemarks() : null);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderStatusUpdateResponse markOutForDelivery(Long orderId, OrderStatusUpdateRequest req) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        OrderStatus previous = order.getStatus();
        orderStateValidator.validate(previous, OrderStatus.OUT_FOR_DELIVERY);

        Order updated = lifecycleService.markOutForDelivery(orderId);

        log.info("AUDIT order={} prev={} curr={} actor={} time={}", orderId, previous, updated.getStatus(),
                req != null ? req.getActor() : null, LocalDateTime.now());

        return AdminOrderMapper.toResponse(updated, previous, req != null ? req.getRemarks() : null);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderStatusUpdateResponse deliverOrder(Long orderId, OrderStatusUpdateRequest req) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        OrderStatus previous = order.getStatus();
        orderStateValidator.validate(previous, OrderStatus.DELIVERED);

        Order updated = lifecycleService.deliverOrder(orderId);

        log.info("AUDIT order={} prev={} curr={} actor={} time={}", orderId, previous, updated.getStatus(),
                req != null ? req.getActor() : null, LocalDateTime.now());

        return AdminOrderMapper.toResponse(updated, previous, req != null ? req.getRemarks() : null);
    }
}
