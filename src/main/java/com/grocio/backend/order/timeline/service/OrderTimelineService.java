package com.grocio.backend.order.timeline.service;

import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.entity.OrderStatusHistory;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.repository.OrderRepository;
import com.grocio.backend.order.repository.OrderStatusHistoryRepository;
import com.grocio.backend.order.timeline.dto.OrderTimelineEvent;
import com.grocio.backend.order.timeline.dto.OrderTimelineResponse;
import com.grocio.backend.order.timeline.mapper.OrderTimelineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTimelineService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public OrderTimelineResponse getOrderTimeline(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        validateOrderOwnership(order, userId);

        List<OrderStatusHistory> historyList = historyRepository.findByOrderIdOrderByChangedAtAsc(orderId);
        List<OrderTimelineEvent> timelineEvents = historyList.stream()
                .map(OrderTimelineMapper::toEvent)
                .toList();

        return OrderTimelineResponse.builder()
                .orderId(orderId)
                .currentStatus(order.getStatus())
                .timeline(timelineEvents)
                .build();
    }

    private void validateOrderOwnership(Order order, Long userId) {
        if (!order.getUserId().equals(userId)) {
            throw new OrderNotFoundException("Order not found with ID: " + order.getOrderId());
        }
    }
}
