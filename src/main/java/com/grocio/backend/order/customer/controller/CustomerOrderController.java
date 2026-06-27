package com.grocio.backend.order.customer.controller;

import com.grocio.backend.order.customer.dto.OrderDetailsResponse;
import com.grocio.backend.order.customer.dto.OrderSummaryResponse;
import com.grocio.backend.order.customer.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @GetMapping
    public ResponseEntity<Page<OrderSummaryResponse>> getCustomerOrders(
            @RequestHeader(name = "userId", required = true) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @PageableDefault(size = 10, page = 0, sort = "orderId", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<OrderSummaryResponse> orders;

        if (status != null && dateFrom != null && dateTo != null) {
            orders = customerOrderService.getCustomerOrdersWithFilters(userId, status, dateFrom, dateTo, pageable);
        } else if (status != null) {
            orders = customerOrderService.getCustomerOrdersByStatus(userId, status, pageable);
        } else if (dateFrom != null && dateTo != null) {
            orders = customerOrderService.getCustomerOrdersByDateRange(userId, dateFrom, dateTo, pageable);
        } else {
            orders = customerOrderService.getCustomerOrders(userId, pageable);
        }

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(
            @PathVariable Long orderId,
            @RequestHeader(name = "userId", required = true) Long userId) {
        OrderDetailsResponse orderDetails = customerOrderService.getOrderDetails(orderId, userId);
        return ResponseEntity.ok(orderDetails);
    }
}
