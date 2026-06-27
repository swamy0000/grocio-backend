package com.grocio.backend.financial.payment.repository;

import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(String paymentReference);

    List<Payment> findByUserId(Long userId);

    List<Payment> findByOrder_OrderId(Long orderId);

    Optional<Payment> findByGatewayPaymentId(String gatewayPaymentId);

    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);

    List<Payment> findByStatus(PaymentStatus status);
}
