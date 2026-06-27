package com.grocio.backend.financial.payment.service;

import com.grocio.backend.financial.payment.dto.PaymentRequest;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.financial.payment.exception.PaymentException;
import com.grocio.backend.financial.payment.internal.PaymentReferenceGenerator;
import com.grocio.backend.financial.payment.mapper.PaymentMapper;
import com.grocio.backend.financial.payment.repository.PaymentRepository;
import com.grocio.backend.financial.payment.factory.PaymentProcessorFactory;
import com.grocio.backend.financial.shared.enums.PaymentMethod;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentReferenceGenerator paymentReferenceGenerator;
    private final com.grocio.backend.financial.payment.internal.PaymentLifecycleValidator paymentLifecycleValidator;
    private final PaymentProcessorFactory paymentProcessorFactory;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        if (request == null) {
            throw new PaymentException("Payment request must not be null");
        }

        Long orderId = request.getOrderId();
        if (orderId == null) {
            throw new PaymentException("Order ID is required to create payment");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new PaymentException("Order not found for id: " + orderId));

        if (request.getAmount() == null) {
            throw new PaymentException("Payment amount must not be null");
        }

        if (request.getAmount().signum() <= 0) {
            throw new PaymentException("Payment amount must be greater than zero");
        }

        if (request.getPaymentMethod() == null) {
            throw new PaymentException("Payment method is required");
        }

        String paymentReference = paymentReferenceGenerator.generatePaymentReference();

        Payment payment = PaymentMapper.toEntity(request, order);
        payment.setPaymentReference(paymentReference);

        Payment savedPayment = paymentRepository.save(payment);
        // If the payment method requires a gateway, create a gateway order and persist
        // gateway info
        if (request.getPaymentMethod() == PaymentMethod.RAZORPAY) {
            try {
                // Prepare gateway order request
                com.grocio.backend.financial.payment.gateway.dto.GatewayOrderRequest gatewayRequest = com.grocio.backend.financial.payment.gateway.dto.GatewayOrderRequest
                        .builder()
                        .amount(convertToPaise(savedPayment.getAmount()))
                        .currency(savedPayment.getCurrency())
                        .receipt(savedPayment.getPaymentReference())
                        .notes(java.util.Map.of(
                                "paymentReference", savedPayment.getPaymentReference(),
                                "orderId", String.valueOf(savedPayment.getOrder().getOrderId()),
                                "userId", String.valueOf(savedPayment.getUserId())))
                        .build();

                com.grocio.backend.financial.payment.gateway.PaymentProcessor processor = paymentProcessorFactory
                        .getProcessor(com.grocio.backend.financial.shared.enums.PaymentGateway.RAZORPAY);

                com.grocio.backend.financial.payment.gateway.dto.GatewayOrderResponse gatewayResponse = processor
                        .createGatewayOrder(savedPayment, gatewayRequest);

                // persist gateway order id and gateway enum (dirty checking will flush on
                // transaction commit)
                savedPayment.setGatewayOrderId(gatewayResponse.getGatewayOrderId());
                savedPayment.setPaymentGateway(com.grocio.backend.financial.shared.enums.PaymentGateway.RAZORPAY);
                savedPayment = paymentRepository.save(savedPayment);
            } catch (RuntimeException ex) {
                // Wrap and rethrow as PaymentException
                throw new PaymentException("Failed to create gateway order: " + ex.getMessage(), ex);
            }
        }

        return PaymentMapper.toResponse(savedPayment);
    }

    private Long convertToPaise(java.math.BigDecimal amount) {
        if (amount == null)
            return 0L;
        java.math.BigDecimal paise = amount.multiply(java.math.BigDecimal.valueOf(100)).setScale(0,
                java.math.RoundingMode.HALF_UP);
        return paise.longValueExact();
    }

    @Override
    public PaymentResponse createPaymentForOrder(Order order, PaymentMethod paymentMethod) {
        if (order == null) {
            throw new PaymentException("Order must not be null");
        }

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(order.getOrderId());
        request.setUserId(order.getUserId());
        request.setAmount(java.math.BigDecimal.valueOf(order.getTotalAmount()));
        request.setPaymentMethod(paymentMethod);

        return createPayment(request);
    }

    @Override
    public PaymentResponse getPayment(String paymentReference) {
        if (paymentReference == null || paymentReference.isBlank()) {
            throw new PaymentException("Payment reference must not be blank");
        }

        Payment payment = paymentRepository.findByPaymentReference(paymentReference)
                .orElseThrow(() -> new PaymentException("Payment not found for reference: " + paymentReference));

        return PaymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByUser(Long userId) {
        if (userId == null) {
            throw new PaymentException("User ID must not be null");
        }

        List<Payment> payments = paymentRepository.findByUserId(userId);
        return payments.stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByOrder(Long orderId) {
        if (orderId == null) {
            throw new PaymentException("Order ID must not be null");
        }

        List<Payment> payments = paymentRepository.findByOrder_OrderId(orderId);
        return payments.stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }

    @Override
    public PaymentResponse updatePaymentStatus(String paymentReference, PaymentStatus status) {
        if (paymentReference == null || paymentReference.isBlank()) {
            throw new PaymentException("Payment reference must not be blank");
        }

        if (status == null) {
            throw new PaymentException("Payment status must not be null");
        }

        Payment payment = paymentRepository.findByPaymentReference(paymentReference)
                .orElseThrow(() -> new PaymentException("Payment not found for reference: " + paymentReference));

        paymentLifecycleValidator.validateTransition(payment.getStatus(), status);

        payment.setStatus(status);

        if (status == PaymentStatus.SUCCESS) {
            payment.setCompletedAt(java.time.LocalDateTime.now());
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentMapper.toResponse(saved);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public com.grocio.backend.financial.payment.dto.PaymentVerificationResponse verifyPayment(
            com.grocio.backend.financial.payment.dto.PaymentVerificationRequest request) {
        if (request == null
                || request.getPaymentReference() == null || request.getPaymentReference().isBlank()
                || request.getRazorpayOrderId() == null || request.getRazorpayOrderId().isBlank()
                || request.getRazorpayPaymentId() == null || request.getRazorpayPaymentId().isBlank()
                || request.getRazorpaySignature() == null || request.getRazorpaySignature().isBlank()) {
            throw new PaymentException("Invalid verification request: missing required fields");
        }

        com.grocio.backend.financial.payment.entity.Payment payment = paymentRepository
                .findByPaymentReference(request.getPaymentReference())
                .orElseThrow(() -> new PaymentException(
                        "Payment not found for reference: " + request.getPaymentReference()));

        if (payment.getStatus() != com.grocio.backend.financial.shared.enums.PaymentStatus.CREATED) {
            throw new PaymentException("Payment is not in CREATED state and cannot be verified");
        }

        // Resolve processor based on payment gateway
        com.grocio.backend.financial.shared.enums.PaymentGateway gateway = payment.getPaymentGateway();
        if (gateway == null || gateway == com.grocio.backend.financial.shared.enums.PaymentGateway.NONE) {
            throw new PaymentException("Payment gateway not configured for this payment");
        }

        com.grocio.backend.financial.payment.gateway.PaymentProcessor processor = paymentProcessorFactory
                .getProcessor(gateway);

        com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationRequest gvReq = new com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationRequest();
        gvReq.setGatewayOrderId(request.getRazorpayOrderId());
        gvReq.setGatewayPaymentId(request.getRazorpayPaymentId());
        gvReq.setGatewaySignature(request.getRazorpaySignature());

        com.grocio.backend.financial.payment.gateway.dto.GatewayVerificationResponse gvResp = processor
                .verifyPayment(gvReq);

        if (!gvResp.isVerified()) {
            throw new PaymentException("Payment signature verification failed");
        }

        // Update payment to SUCCESS and persist gateway payment id
        payment.setStatus(com.grocio.backend.financial.shared.enums.PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(
                gvResp.getGatewayPaymentId() != null ? gvResp.getGatewayPaymentId() : request.getRazorpayPaymentId());
        payment.setCompletedAt(java.time.LocalDateTime.now());

        paymentRepository.save(payment);

        com.grocio.backend.financial.payment.dto.PaymentVerificationResponse resp = new com.grocio.backend.financial.payment.dto.PaymentVerificationResponse();
        resp.setVerified(true);
        resp.setPaymentReference(payment.getPaymentReference());
        resp.setPaymentStatus(payment.getStatus().name());
        resp.setMessage("verified");

        return resp;
    }
}
