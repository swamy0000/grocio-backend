package com.grocio.backend.order.service;

import com.grocio.backend.entity.Payment1;
import com.grocio.backend.entity.User;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.lifecycle.OrderLifecycleService;
import com.grocio.backend.repository.PaymentRepository1;
import com.grocio.backend.repository.UserRepository;
import com.grocio.backend.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderPaymentService {

    private final PaymentRepository1 paymentRepository;
    private final UserRepository userRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final OrderLifecycleService orderLifecycleService;

    @Transactional(rollbackFor = Exception.class)
    public void processPayment(Order order, User user) {
        BigDecimal orderAmount = BigDecimal.valueOf(order.getTotalAmount());

        if ("WALLET".equals(order.getPaymentMethod())) {
            BigDecimal currentWallet = user.getWalletBalance() != null ? user.getWalletBalance() : BigDecimal.ZERO;
            if (currentWallet.compareTo(orderAmount) < 0) {
                throw new RuntimeException("Insufficient Wallet Balance! Please add money.");
            }

            BigDecimal newBalance = currentWallet.subtract(orderAmount);
            user.setWalletBalance(newBalance);
            userRepository.save(user);

            com.grocio.backend.entity.WalletTransaction wt = new com.grocio.backend.entity.WalletTransaction();
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
            orderLifecycleService.confirmOrder(order.getOrderId(), null);
        } else if ("COD".equals(order.getPaymentMethod())) {
            order.setPaymentStatus("UNPAID");
            orderLifecycleService.confirmOrder(order.getOrderId(), null);
        } else {
            throw new RuntimeException("Unsupported payment method: " + order.getPaymentMethod());
        }

        Payment1 payment = new Payment1();
        payment.setOrderId(order.getOrderId());
        payment.setPaymentMode(order.getPaymentMethod());
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(order.getPaymentStatus());
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }
}
