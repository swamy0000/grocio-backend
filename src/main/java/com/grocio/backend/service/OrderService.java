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
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private OrderStatusHistoryRepository historyRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Transactional(rollbackFor = Exception.class)
    public Long placeOrder(OrderRequestDTO request) {
        
        // 1. User & Wallet Balance వెరిఫికేషన్
        User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found!"));
        
        java.math.BigDecimal orderAmount = java.math.BigDecimal
        .valueOf(request.getTotalAmount() != null ? request.getTotalAmount() : 0.0);
        java.math.BigDecimal currentWallet = user.getWalletBalance() != null ? user.getWalletBalance()
        : java.math.BigDecimal.ZERO;
        
        if (currentWallet.compareTo(orderAmount) < 0) {
            throw new RuntimeException("Insufficient Wallet Balance! Please add money.");
        }
        
        // 2. ఆర్డర్ ఆబ్జెక్ట్ బిల్డ్ చేయడం (ఎగ్జిస్టింగ్ కాలమ్స్ ప్రకారం)
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setDeliveryAddressId(request.getDeliveryAddressId());
        order.setTotalAmount(request.getTotalAmount());
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "Wallet");
        order.setPaymentStatus("SUCCESS"); // వాలెట్ నుండి కట్ అవుతుంది కాబట్టి
        order.setStatus("ORDER_PLACED"); // ఇండస్ట్రీ స్టాండర్డ్ ఇనిషియల్ స్టేట్
        order.setOrderTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0);
        order.setHandlingCharge(request.getHandlingCharge() != null ? request.getHandlingCharge() : 5.0);
        order.setDeliveryLatitude(request.getLatitude());
        order.setDeliveryLongitude(request.getLongitude());
        
        // 🟢 మ్యాజిక్ 1: రాండమ్ 4-Digit Secure Delivery OTP జెనరేట్ చేయడం
        String randomOtp = String.format("%04d", new Random().nextInt(10000));
        order.setDeliveryOtp(randomOtp);
        
        // 3. ఇన్వెంటరీ (Stock Check & Deduct) మరియు ఐటెమ్స్ మ్యాపింగ్
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty! Cannot place order.");
        }
        
        for (OrderItemRequestDTO itemDto : request.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));
            
            int currentStock = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
            if (currentStock < itemDto.getQuantity()) {
                throw new RuntimeException(
                    "Out of stock for item: " + product.getName() + " (Only " + currentStock + " left)");
                }
                
                // స్టాక్ తగ్గించి డేటాబేస్ లో సేవ్ చేయడం
                product.setStockQuantity(currentStock - itemDto.getQuantity());
                productRepository.save(product);
                
                // OrderItem క్రియేషన్
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemDto.getQuantity());
                orderItem.setPriceAtThatTime(
                    itemDto.getPriceAtThatTime() != null ? itemDto.getPriceAtThatTime() : product.getPrice());
                    
                    order.getItems().add(orderItem);
                }
                
                // 4. యూజర్ వాలెట్ డిడక్షన్
                user.setWalletBalance(currentWallet.subtract(orderAmount));
                userRepository.save(user);
                
                // 5. ఆర్డర్ మరియు ఐటెమ్స్ ని ఒకేసారి సేవ్ చేయడం
                Order savedOrder = orderRepository.save(order);
                
                // 🟢 మ్యాజిక్ 2: టైమ్‌లైన్ ట్రాకింగ్ కోసం మొదటి హిస్టరీ లాగ్ సేవ్ చేయడం
                OrderStatusHistory history = new OrderStatusHistory();
                history.setOrderId(savedOrder.getOrderId());
                history.setStatus("ORDER_PLACED");
                history.setChangedAt(LocalDateTime.now());
                history.setRemarks("Order placed successfully. Waiting for Store acceptance.");
                historyRepository.save(history);
                
                // 🟢 మ్యాజిక్ 3: ఆర్డర్ ప్లేస్ అయిపోయింది కాబట్టి యూజర్ యాక్టివ్ కార్ట్ ని
                // క్లియర్ చేయడం
                cartRepository.findByUserId(request.getUserId()).ifPresent(cart -> {
                    cartItemRepository.deleteAllByCartId(cart.getCartId());
                });
                
                messagingTemplate.convertAndSend("/topic/store/orders", "NEW_ORDER_PLACED");
                return savedOrder.getOrderId();
            }
            
            // 🟢 1. స్టోర్ డ్యాష్‌బోర్డ్ కోసం నిర్దిష్ట స్టేటస్ గల ఆర్డర్లను పొందడం
            public List<Order> getOrdersByStatus(String status) {
                return orderRepository.findByStatusOrderByOrderTimeDesc(status);
            }
            
            // 🟢 2. ఆర్డర్ స్టేట్ మెషిన్ (Status Updater with History Logging)
            @Transactional(rollbackFor = Exception.class)
            public Order updateOrderStatus(Long orderId, String newStatus, Long partnerId, String remarks) {
                Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
                
                // స్టేటస్ మార్చడం
                order.setStatus(newStatus);
                order.setUpdatedAt(LocalDateTime.now());
                
                // డెలివరీ బాయ్ అసైన్ అయితే ఐడీ అప్‌డేట్ చేస్తాం
                if (partnerId != null) {
                    order.setDeliveryPartnerId(partnerId);
                }
                
                // ఒకవేళ ఆర్డర్ డెలివరీ అయిపోతే పేమెంట్ స్టేటస్ ని 'COMPLETED' చేస్తాం
                if ("DELIVERED".equalsIgnoreCase(newStatus)) {
                    order.setPaymentStatus("COMPLETED");
                }
                
                Order updatedOrder = orderRepository.save(order);
                
                // 🟢 ప్రతి స్టేటస్ మారినప్పుడల్లా ఆటోమేటిక్ గా హిస్టరీ టేబుల్ లో లాగ్ సేవ్
                // అవుతుంది
                OrderStatusHistory history = new OrderStatusHistory();
                history.setOrderId(orderId);
                history.setStatus(newStatus);
                history.setChangedAt(LocalDateTime.now());
                history.setRemarks(remarks != null ? remarks : "Order status updated to " + newStatus);
                historyRepository.save(history);
                messagingTemplate.convertAndSend("/topic/order/" + orderId, newStatus);
                return updatedOrder;
            }
            
            // 🟢 డెలివరీ బాయ్ ఎంటర్ చేసిన OTP ని వెరిఫై చేసి ఆర్డర్ ని DELIVERED మార్చే
            // ప్రొఫెషనల్ లాజిక్
            @Transactional(rollbackFor = Exception.class)
            public void verifyOtpAndDeliver(Long orderId, String inputOtp) {
                Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
                
                // 1. ఓటీపీ చెక్ చేయడం
                if (!order.getDeliveryOtp().equals(inputOtp)) {
                    throw new RuntimeException("Invalid Delivery OTP! Please check with the customer.");
                }
                
                // 2. అంతా కరెక్ట్ అయితే స్టేటస్ అప్‌డేట్
                order.setStatus("DELIVERED");
                order.setPaymentStatus("COMPLETED");
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                
                // 3. హిస్టరీ టేబుల్ లో లాగ్ సేవ్ చేయడం
                OrderStatusHistory history = new OrderStatusHistory();
                history.setOrderId(orderId);
                history.setStatus("DELIVERED");
                history.setChangedAt(LocalDateTime.now());
                history.setRemarks("Order successfully delivered via Secure OTP Verification.");
                historyRepository.save(history);
            }
        }