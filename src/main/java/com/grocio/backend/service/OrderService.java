package com.grocio.backend.service;

import com.grocio.backend.dto.*;
import com.grocio.backend.entity.*;
import com.grocio.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderStatusHistoryRepository historyRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    
    // 🟢 కొత్తగా యాడ్ చేసిన Enterprise Repositories
    @Autowired private PaymentRepository paymentRepository; 
    @Autowired private WalletTransactionRepository walletTransactionRepository;
    @Autowired private CartCouponRepository cartCouponRepository;

    @Autowired private SimpMessagingTemplate messagingTemplate;

    // 🛡️ పక్కా ప్రొడక్షన్ రూల్: ఏ ఒక్కటి ఫెయిల్ అయినా మొత్తం రోల్‌బ్యాక్ (Cancel) అవుతుంది!
    @Transactional(rollbackFor = Exception.class)
    public Long placeOrder(OrderRequestDTO request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // 1. Initial Order Creation (ఎంటర్‌ప్రైజ్ రూల్ ప్రకారం ముందు PENDING_PAYMENT తో ఆర్డర్ క్రియేట్ అవుతుంది)
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setDeliveryAddressId(request.getDeliveryAddressId());
        
        // 🟢 పాత ఫీల్డ్స్ తో పాటు కొత్త ఫీల్డ్స్ (DTO నుండి వస్తాయని అనుకుంటున్నాం)
        order.setItemTotal(request.getItemTotal() != null ? request.getItemTotal() : request.getTotalAmount());
        order.setCouponDiscount(request.getCouponDiscount() != null ? request.getCouponDiscount() : 0.0);
        
        order.setTotalAmount(request.getTotalAmount());
        order.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0);
        order.setHandlingCharge(request.getHandlingCharge() != null ? request.getHandlingCharge() : 5.0);
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : "COD");

        order.setPaymentStatus("PENDING"); 
        order.setStatus("PENDING_PAYMENT"); 
        order.setOrderTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDeliveryLatitude(request.getLatitude());
        order.setDeliveryLongitude(request.getLongitude());

        // Secure OTP Generation
        String randomOtp = String.format("%04d", new Random().nextInt(10000));
        order.setDeliveryOtp(randomOtp);

        Order savedOrder = orderRepository.save(order);

        // 2. Inventory Check & Deduct (స్టాక్ ఉందో లేదో చెక్ చేసి మైనస్ చేయడం)
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty! Cannot place order.");
        }

        for (OrderItemRequestDTO itemDto : request.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));

            int currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
            if (currentStock < itemDto.getQuantity()) {
                throw new RuntimeException("Out of stock for item: " + product.getName() + " (Only " + currentStock + " left)");
            }

            product.setStockQuantity(currentStock - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtThatTime(itemDto.getPriceAtThatTime() != null ? itemDto.getPriceAtThatTime() : product.getPrice());

            savedOrder.getItems().add(orderItem);
        }

        // 3. Payment Processing Engine (Strategy Pattern)
        processPayment(savedOrder, user);

        // అప్‌డేట్ అయిన స్టేటస్ తో ఫైనల్ గా ఆర్డర్ సేవ్ చేయడం
        orderRepository.save(savedOrder);

        // 4. Record Initial Order History
        recordOrderHistory(savedOrder.getOrderId(), savedOrder.getStatus(), "Order successfully placed via " + savedOrder.getPaymentMethod());

        // 5. Cleanup: కార్ట్ మరియు అప్లై అయిన కూపన్ ని క్లియర్ చేయడం
        cartRepository.findByUserId(request.getUserId()).ifPresent(cart -> {
            cartItemRepository.deleteAllByCartId(cart.getCartId());
        });
        cartCouponRepository.deleteById(request.getUserId());

        // 6. Notify Store Dashboard
        messagingTemplate.convertAndSend("/topic/store/orders", "NEW_ORDER_PLACED");

        return savedOrder.getOrderId();
    }

    // 💳 THE PAYMENT STRATEGY METHOD
    private void processPayment(Order order, User user) {
        java.math.BigDecimal orderAmount = java.math.BigDecimal.valueOf(order.getTotalAmount());

        if ("WALLET".equals(order.getPaymentMethod())) {
            java.math.BigDecimal currentWallet = user.getWalletBalance() != null ? user.getWalletBalance() : java.math.BigDecimal.ZERO;

            if (currentWallet.compareTo(orderAmount) < 0) {
                // ఎర్రర్ వస్తే ఆటోమేటిక్ గా స్ప్రింగ్ బూట్ స్టాక్ తగ్గింపును రోల్ బ్యాక్ చేస్తుంది!
                throw new RuntimeException("Insufficient Wallet Balance! Please add money."); 
            }

            // Wallet Deduction
            java.math.BigDecimal newBalance = currentWallet.subtract(orderAmount);
            user.setWalletBalance(newBalance);
            userRepository.save(user);

            // 🟢 Wallet Transaction History Tracking
            WalletTransaction wt = new WalletTransaction();
            wt.setUserId(user.getUserId());
            wt.setOrderId(order.getOrderId());
            wt.setType("DEBIT");
            wt.setAmount(order.getTotalAmount());
            wt.setBalanceBefore(currentWallet.doubleValue());
            wt.setBalanceAfter(newBalance.doubleValue());
            wt.setDescription("Paid for Order #" + order.getOrderId());
            wt.setCreatedAt(LocalDateTime.now());
            walletTransactionRepository.save(wt);

            order.setPaymentStatus("PAID");
            order.setStatus("PLACED");

        } else if ("COD".equals(order.getPaymentMethod())) {
            order.setPaymentStatus("UNPAID");
            order.setStatus("PLACED");
        } else {
            throw new RuntimeException("Unsupported payment method: " + order.getPaymentMethod());
        }

        // 🟢 Save to Main Payments Table (For Order Details screen)
        Payment payment = new Payment();
        payment.setOrderId(order.getOrderId());
        payment.setPaymentMode(order.getPaymentMethod());
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(order.getPaymentStatus());
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    // 📜 HELPER: History Tracker
    private void recordOrderHistory(Long orderId, String status, String remarks) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrderId(orderId);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());
        history.setRemarks(remarks);
        historyRepository.save(history);
    }

    // ==========================================
    // పాత ఫంక్షన్స్ అన్నీ అలాగే ఉంచేశాను, సేఫ్ గా!
    // ==========================================

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatusOrderByOrderTimeDesc(status);
    }

    @Transactional(rollbackFor = Exception.class)
    public Order updateOrderStatus(Long orderId, String newStatus, Long partnerId, String remarks) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if (partnerId != null) {
            order.setDeliveryPartnerId(partnerId);
        }

        if ("DELIVERED".equalsIgnoreCase(newStatus)) {
            order.setPaymentStatus("COMPLETED");
        }

        Order updatedOrder = orderRepository.save(order);
        recordOrderHistory(orderId, newStatus, remarks != null ? remarks : "Order status updated to " + newStatus);
        
        messagingTemplate.convertAndSend("/topic/order/" + orderId, newStatus);
        return updatedOrder;
    }

    @Transactional(rollbackFor = Exception.class)
    public void verifyOtpAndDeliver(Long orderId, String inputOtp) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));

        if (!order.getDeliveryOtp().equals(inputOtp)) {
            throw new RuntimeException("Invalid Delivery OTP! Please check with the customer.");
        }

        order.setStatus("DELIVERED");
        order.setPaymentStatus("COMPLETED");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        recordOrderHistory(orderId, "DELIVERED", "Order successfully delivered via Secure OTP Verification.");
    }
}