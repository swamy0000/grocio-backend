package com.grocio.backend.order.cancellation.service;

import com.grocio.backend.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundTriggerService {

    private static final Logger log = LoggerFactory.getLogger(RefundTriggerService.class);

    /**
     * Placeholder to trigger a refund. Only logs the request.
     */
    public void triggerRefund(Order order, String actor, String reason) {
        log.info("REFUND_TRIGGER orderId={} actor={} amount={} reason={}", order.getOrderId(), actor,
                order.getTotalAmount(), reason);
    }
}
