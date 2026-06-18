package com.grocio.backend.controller;

import com.grocio.backend.dto.OrderRequestDTO;
import com.grocio.backend.entity.Order;
import com.grocio.backend.entity.PaymentMode;
import com.grocio.backend.repository.OrderRepository;
import com.grocio.backend.repository.PaymentModeRepository;
import com.grocio.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate; // 🟢 ఇక్కడ కొత్త ఇంపోర్ట్ యాడ్ చేసాం
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentModeRepository paymentModeRepository;

    // 🟢 0. THE WEBSOCKET MESSENGER: ఫ్లట్టర్ కి లైవ్ సిగ్నల్స్ పంపే టూల్
    @Autowired
    private SimpMessagingTemplate messagingTemplate; 

    @GetMapping("/payment-modes")
    public ResponseEntity<List<PaymentMode>> getActivePaymentModes() {
        return ResponseEntity.ok(paymentModeRepository.findByEnabledTrueOrderByDisplayOrderAsc());
    }

    @PostMapping("/place")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody OrderRequestDTO orderRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long orderId = orderService.placeOrder(orderRequest);
            response.put("success", true);
            response.put("message", "Order placed successfully!");
            response.put("orderId", orderId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException re) {
            response.put("success", false);
            response.put("message", re.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Internal server error occurred. Please try again.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    // 🟢 2. స్టేటస్ అప్‌డేట్ & వెబ్‌సాకెట్ బ్రాడ్‌కాస్ట్
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) String remarks) {

        Map<String, Object> response = new HashMap<>();
        try {
            // డేటాబేస్ లో ఆర్డర్ అప్‌డేట్ అవుతుంది
            Order order = orderService.updateOrderStatus(orderId, status, partnerId, remarks);
            
            // ⚡ WEBSOCKET MAGIC ⚡
            // 1. My Orders లిస్ట్ రిఫ్రెష్ అవ్వడానికి (User Topic)
            messagingTemplate.convertAndSend("/topic/user/" + order.getUserId() + "/orders", order.getStatus());
            
            // 2. లైవ్ ట్రాకింగ్ మ్యాప్ రిఫ్రెష్ అవ్వడానికి (Order Topic)
            messagingTemplate.convertAndSend("/topic/order/" + orderId, order.getStatus());

            response.put("success", true);
            response.put("message", "Order status updated to " + status);
            response.put("currentStatus", order.getStatus());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{orderId}/verify-delivery")
    public ResponseEntity<Map<String, Object>> verifyDeliveryOtp(
            @PathVariable Long orderId,
            @RequestParam String otp) {

        Map<String, Object> response = new HashMap<>();
        try {
            orderService.verifyOtpAndDeliver(orderId, otp);
            
            // OTP వెరిఫై అయితే ఆర్డర్ DELIVERED కి వెళ్తుంది కాబట్టి, ఆ సిగ్నల్ కూడా పంపుతున్నాం
            Order order = orderRepository.findById(orderId).orElseThrow();
            messagingTemplate.convertAndSend("/topic/user/" + order.getUserId() + "/orders", "DELIVERED");
            messagingTemplate.convertAndSend("/topic/order/" + orderId, "DELIVERED");

            response.put("success", true);
            response.put("message", "Order delivered successfully! Payment released.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException re) {
            response.put("success", false);
            response.put("message", re.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Internal server error.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> userOrders = orderRepository.findByUserIdOrderByOrderTimeDesc(userId);
        return ResponseEntity.ok(userOrders);
    }
}