package com.grocio.backend.order.timeline.dto;

import com.grocio.backend.order.lifecycle.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTimelineResponse {
    private Long orderId;
    private OrderStatus currentStatus;
    private List<OrderTimelineEvent> timeline;
}
