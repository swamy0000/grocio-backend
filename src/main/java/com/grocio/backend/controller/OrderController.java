package com.grocio.backend.controller;

import com.grocio.backend.dto.OrderRequestDTO;
import com.grocio.backend.entity.Order;
import com.grocio.backend.entity.PaymentMode;
import com.grocio.backend.repository.OrderRepository;
import com.grocio.backend.repository.PaymentModeRepository;
import com.grocio.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private PaymentModeRepository paymentModeRepository; // 🟢 డైనమిక్ UI కోసం కొత్తగా యాడ్ చేశాం

    // 🟢 0. (క్రొత్తది) ఫ్లట్టర్ పేమెంట్ స్క్రీన్ UI డైనమిక్ గా బిల్డ్ చేయడానికి
    @GetMapping("/payment-modes")
    public ResponseEntity<List<PaymentMode>> getActivePaymentModes() {
        // enabled = true ఉన్న ఆప్షన్స్ మాత్రమే ఫ్లట్టర్ కి వెళ్తాయి
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

    // 🟢 1. స్టోర్ మేనేజర్ కోసం: కొత్తగా వచ్చిన ఆర్డర్లను చూడటానికి
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    // 🟢 2. స్టేటస్ అప్‌డేట్ చేయడానికి: స్టోర్ & డెలివరీ ఏజెంట్ ఇద్దరూ దీన్నే వాడతారు
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long orderId,
            @RequestParam String status,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) String remarks) {

        Map<String, Object> response = new HashMap<>();
        try {
            Order order = orderService.updateOrderStatus(orderId, status, partnerId, remarks);
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

    // 🟢 3. డెలివరీ ఓటీపీ వెరిఫికేషన్ ఎండ్ పాయింట్
    @PostMapping("/{orderId}/verify-delivery")
    public ResponseEntity<Map<String, Object>> verifyDeliveryOtp(
            @PathVariable Long orderId,
            @RequestParam String otp) {

        Map<String, Object> response = new HashMap<>();
        try {
            orderService.verifyOtpAndDeliver(orderId, otp);
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

    // 🟢 4. ఒక నిర్దిష్ట ఆర్డర్ యొక్క పూర్తి వివరాలు పొందడానికి
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        return orderRepository.findById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🟢 5. ఒక కస్టమర్ యొక్క పాత ఆర్డర్ హిస్టరీ మొత్తం పొందడానికి (GET /api/orders/user/1)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> userOrders = orderRepository.findByUserIdOrderByOrderTimeDesc(userId);
        return ResponseEntity.ok(userOrders);
    }
}