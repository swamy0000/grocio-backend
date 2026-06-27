package com.grocio.backend.order.service;

import com.grocio.backend.order.dto.OrderResponse;
import com.grocio.backend.order.entity.OrderStatusHistory;
import com.grocio.backend.order.lifecycle.OrderStatus;
import com.grocio.backend.order.mapper.OrderMapper;
import com.grocio.backend.order.repository.OrderRepository;
import com.grocio.backend.order.repository.OrderStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

    private final OrderRepository orderRepository;
    private final OrderStatusHistoryRepository historyRepository;

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserIdOrderByOrderTimeDesc(userId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void recordOrderHistory(Long orderId, OrderStatus fromStatus, OrderStatus toStatus, String actor,
            String remarks) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrderId(orderId);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setActor(actor);
        history.setChangedAt(LocalDateTime.now());
        history.setRemarks(remarks);
        historyRepository.save(history);
    }
}
