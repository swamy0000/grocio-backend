package com.grocio.backend.order.service;

import com.grocio.backend.cart.repository.CartCouponRepository;
import com.grocio.backend.cart.repository.CartItemRepository;
import com.grocio.backend.cart.repository.CartRepository;
import com.grocio.backend.entity.User;
import com.grocio.backend.order.dto.OrderItemRequest;
import com.grocio.backend.order.dto.OrderPlacementResponse;
import com.grocio.backend.order.dto.OrderRequest;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.entity.OrderItem;
import com.grocio.backend.order.repository.OrderRepository;
import com.grocio.backend.order.util.OtpGenerator;
import com.grocio.backend.order.validator.OrderValidator;
import com.grocio.backend.product.entity.Product;
import com.grocio.backend.product.repository.ProductRepository;
import com.grocio.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderPlacementService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartCouponRepository cartCouponRepository;
    private final OrderValidator orderValidator;
    private final OrderPaymentService orderPaymentService;
    private final OrderHistoryService orderHistoryService;
    private final OrderNotificationService orderNotificationService;
    private final InventoryService inventoryService;
    private final OtpGenerator otpGenerator;

    @Transactional(rollbackFor = Exception.class)
    public OrderPlacementResponse placeOrder(OrderRequest request) {
        return placeOrder(request, false);
    }

    @Transactional(rollbackFor = Exception.class)
    public OrderPlacementResponse placeOrder(OrderRequest request, boolean skipPaymentProcessingForGateway) {
        orderValidator.validateOrderRequest(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setDeliveryAddressId(request.getDeliveryAddressId());
        order.setItemTotal(request.getItemTotal() != null ? request.getItemTotal() : request.getTotalAmount());
        order.setCouponDiscount(request.getCouponDiscount() != null ? request.getCouponDiscount() : 0.0);
        order.setTotalAmount(request.getTotalAmount());
        order.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : 0.0);
        order.setHandlingCharge(request.getHandlingCharge() != null ? request.getHandlingCharge() : 5.0);
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod().toUpperCase() : "COD");
        order.setPaymentStatus("PENDING");
        order.setStatus(com.grocio.backend.order.lifecycle.OrderStatus.PENDING_PAYMENT);
        order.setOrderTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDeliveryLatitude(request.getLatitude());
        order.setDeliveryLongitude(request.getLongitude());
        order.setDeliveryOtp(otpGenerator.generateOtp());

        Order savedOrder = orderRepository.save(order);

        for (OrderItemRequest itemDto : request.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));

            // Use InventoryService for stock deduction
            inventoryService.deductStock(itemDto.getProductId(), itemDto.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtThatTime(
                    itemDto.getPriceAtThatTime() != null ? itemDto.getPriceAtThatTime() : product.getPrice());
            savedOrder.getItems().add(orderItem);
        }

        boolean isWalletPayment = "WALLET".equalsIgnoreCase(order.getPaymentMethod());
        boolean isCodPayment = "COD".equalsIgnoreCase(order.getPaymentMethod());
        boolean isGatewayPayment = "RAZORPAY".equalsIgnoreCase(order.getPaymentMethod());

        if (isWalletPayment || isCodPayment) {
            orderPaymentService.processPayment(savedOrder, user);
        } else if (isGatewayPayment && skipPaymentProcessingForGateway) {
            savedOrder.setPaymentStatus("PENDING");
            savedOrder.setStatus(com.grocio.backend.order.lifecycle.OrderStatus.PENDING_PAYMENT);
            orderRepository.save(savedOrder);
        } else {
            throw new RuntimeException("Unsupported payment method: " + request.getPaymentMethod());
        }

        orderRepository.save(savedOrder);
        orderHistoryService.recordOrderHistory(savedOrder.getOrderId(), null, savedOrder.getStatus(), null,
                "Order successfully placed via " + savedOrder.getPaymentMethod());

        cartRepository.findByUserId(request.getUserId())
                .ifPresent(cart -> cartItemRepository.deleteAllByCartId(cart.getCartId()));
        cartCouponRepository.deleteById(request.getUserId());

        // Use OrderNotificationService for WebSocket messaging
        orderNotificationService.notifyNewOrderPlaced();

        return OrderPlacementResponse.success(savedOrder.getOrderId(), "Order placed successfully!");
    }
}
