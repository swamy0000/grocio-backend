package com.grocio.backend.order.cancellation.service;

import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.lifecycle.OrderStatus;
import com.grocio.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReturnRequestService {

    private static final Logger log = LoggerFactory.getLogger(ReturnRequestService.class);

    private final OrderRepository orderRepository;

    @Transactional(rollbackFor = Exception.class)
    public void requestReturn(Long orderId, Long userId, String reason, String actor) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }

        if (!OrderStatus.DELIVERED.equals(order.getStatus())) {
            throw new IllegalStateException("Return can only be requested for delivered orders.");
        }

        // Foundation only: log the return request. Processing/approval not implemented.
        log.info("RETURN_REQUEST orderId={} actor={} reason={}", orderId, actor, reason);
    }
}
