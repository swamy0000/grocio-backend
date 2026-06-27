package com.grocio.backend.financial.payment.dto;

import com.grocio.backend.financial.shared.enums.PaymentGateway;
import com.grocio.backend.financial.shared.enums.PaymentMethod;
import com.grocio.backend.financial.shared.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String paymentReference;
    private Long orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentGateway paymentGateway;
    private PaymentStatus status;
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private LocalDateTime completedAt;
}
