package com.grocio.backend.order.admin.controller;

import com.grocio.backend.order.admin.dto.OrderStatusUpdateRequest;
import com.grocio.backend.order.admin.dto.OrderStatusUpdateResponse;
import com.grocio.backend.order.admin.service.AdminOrderService;
import com.grocio.backend.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @PatchMapping("/{orderId}/pack")
    public ResponseEntity<OrderStatusUpdateResponse> packOrder(@PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest req) {
        try {
            return ResponseEntity.ok(adminOrderService.packOrder(orderId, req));
        } catch (OrderNotFoundException onf) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ise) {
            return ResponseEntity.badRequest().body(OrderStatusUpdateResponse.builder()
                    .orderId(orderId)
                    .message(ise.getMessage())
                    .build());
        }
    }

    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<OrderStatusUpdateResponse> shipOrder(@PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest req) {
        try {
            return ResponseEntity.ok(adminOrderService.shipOrder(orderId, req));
        } catch (OrderNotFoundException onf) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ise) {
            return ResponseEntity.badRequest().body(OrderStatusUpdateResponse.builder()
                    .orderId(orderId)
                    .message(ise.getMessage())
                    .build());
        }
    }

    @PatchMapping("/{orderId}/out-for-delivery")
    public ResponseEntity<OrderStatusUpdateResponse> outForDelivery(@PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest req) {
        try {
            return ResponseEntity.ok(adminOrderService.markOutForDelivery(orderId, req));
        } catch (OrderNotFoundException onf) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ise) {
            return ResponseEntity.badRequest().body(OrderStatusUpdateResponse.builder()
                    .orderId(orderId)
                    .message(ise.getMessage())
                    .build());
        }
    }

    @PatchMapping("/{orderId}/deliver")
    public ResponseEntity<OrderStatusUpdateResponse> deliver(@PathVariable Long orderId,
            @RequestBody OrderStatusUpdateRequest req) {
        try {
            return ResponseEntity.ok(adminOrderService.deliverOrder(orderId, req));
        } catch (OrderNotFoundException onf) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ise) {
            return ResponseEntity.badRequest().body(OrderStatusUpdateResponse.builder()
                    .orderId(orderId)
                    .message(ise.getMessage())
                    .build());
        }
    }
}
