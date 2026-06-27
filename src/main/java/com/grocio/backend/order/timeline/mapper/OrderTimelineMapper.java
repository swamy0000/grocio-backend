package com.grocio.backend.order.timeline.mapper;

import com.grocio.backend.order.entity.OrderStatusHistory;
import com.grocio.backend.order.timeline.dto.OrderTimelineEvent;

public class OrderTimelineMapper {

    private OrderTimelineMapper() {
    }

    public static OrderTimelineEvent toEvent(OrderStatusHistory history) {
        return OrderTimelineEvent.builder()
                .fromStatus(history.getFromStatus())
                .toStatus(history.getToStatus())
                .changedAt(history.getChangedAt())
                .actor(history.getActor())
                .remarks(history.getRemarks())
                .build();
    }
}
