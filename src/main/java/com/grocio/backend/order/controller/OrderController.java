package com.grocio.backend.order.controller;

import com.grocio.backend.order.dto.OrderPlacementResponse;
import com.grocio.backend.order.dto.OrderRequest;
import com.grocio.backend.order.dto.OrderResponse;
import com.grocio.backend.order.dto.OrderStatusUpdateResponse;
import com.grocio.backend.order.dto.PaymentModeResponse;
import com.grocio.backend.order.dto.VerifyDeliveryResponse;
import com.grocio.backend.order.service.OrderFacadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderFacadeService orderFacadeService;

    public OrderController(OrderFacadeService orderFacadeService) {
        this.orderFacadeService = orderFacadeService;
    }

    @GetMapping("/payment-modes")
    public ResponseEntity<List<PaymentModeResponse>> getActivePaymentModes() {
        return ResponseEntity.ok(orderFacadeService.getActivePaymentModes());
    }

    @PostMapping("/place")
    public ResponseEntity<OrderPlacementResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        try {
            return ResponseEntity.ok(orderFacadeService.placeOrder(orderRequest));
        } catch (RuntimeException re) {
            return ResponseEntity.badRequest().body(OrderPlacementResponse.failure(re.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(OrderPlacementResponse.failure("Internal server error occurred. Please try again."));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderFacadeService.getOrdersByStatus(status));
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable String status) {

        return ResponseEntity.ok(
                orderFacadeService.getOrdersByUserIdAndStatus(userId, status));
    }

    @PostMapping("/{orderId}/verify-delivery")
    public ResponseEntity<VerifyDeliveryResponse> verifyDeliveryOtp(
            @PathVariable Long orderId,
            @RequestParam String otp) {
        try {
            return ResponseEntity.ok(orderFacadeService.verifyOtpAndDeliver(orderId, otp));
        } catch (RuntimeException re) {
            return ResponseEntity.badRequest().body(VerifyDeliveryResponse.failure(re.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(VerifyDeliveryResponse.failure("Internal server error."));
        }
    }

    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<OrderStatusUpdateResponse> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) String remarks) {
        try {
            return ResponseEntity.ok(orderFacadeService.updateOrderStatus(orderId, status, partnerId, remarks));
        } catch (RuntimeException re) {
            return ResponseEntity.badRequest().body(OrderStatusUpdateResponse.failure(re.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(OrderStatusUpdateResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        return orderFacadeService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderFacadeService.getOrdersByUserId(userId));
    }
}
