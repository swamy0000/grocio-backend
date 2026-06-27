package com.grocio.backend.order.timeline.controller;

import com.grocio.backend.order.timeline.dto.OrderTimelineResponse;
import com.grocio.backend.order.timeline.service.OrderTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderTimelineController {

    private final OrderTimelineService timelineService;

    @GetMapping("/{orderId}/timeline")
    public ResponseEntity<OrderTimelineResponse> getOrderTimeline(
            @PathVariable Long orderId,
            @RequestHeader(name = "userId", required = true) Long userId) {
        OrderTimelineResponse response = timelineService.getOrderTimeline(orderId, userId);
        return ResponseEntity.ok(response);
    }
}
