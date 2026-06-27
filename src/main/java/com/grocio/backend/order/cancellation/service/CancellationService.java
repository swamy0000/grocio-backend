package com.grocio.backend.order.cancellation.service;

import com.grocio.backend.order.entity.Order;
import com.grocio.backend.order.entity.OrderItem;
import com.grocio.backend.order.exception.OrderNotFoundException;
import com.grocio.backend.order.lifecycle.OrderLifecycleService;
import com.grocio.backend.inventory.service.InventoryReservationService;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.order.lifecycle.OrderStatus;
import com.grocio.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancellationService {

    private static final Logger log = LoggerFactory.getLogger(CancellationService.class);

    private final OrderRepository orderRepository;
    private final OrderLifecycleService lifecycleService;
    private final InventoryReservationService reservationService;
    private final RefundTriggerService refundTriggerService;

    @Transactional(rollbackFor = Exception.class)
    public void customerCancelOrder(Long orderId, Long userId, String reason, String actor) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (userId == null || order.getUserId() == null || !userId.equals(order.getUserId())) {
            throw new OrderNotFoundException("Order not found with ID: " + orderId);
        }

        OrderStatus current = order.getStatus();
        if (!(OrderStatus.CONFIRMED.equals(current) || OrderStatus.PACKED.equals(current))) {
            throw new IllegalStateException("Order cannot be cancelled by customer from status: " + current);
        }

        Order updated = lifecycleService.cancelOrder(orderId);

        // Release reservations associated with this order (do not modify inventory
        // directly)
        try {
            java.util.List<InventoryReservation> reservations = reservationService.getReservationsByOrderId(orderId);
            if (reservations != null) {
                for (InventoryReservation res : reservations) {
                    try {
                        if (res != null) {
                            reservationService.releaseReservation(res.getReservationReference());
                        }
                    } catch (Exception e) {
                        log.warn("Failed to release reservation for order={} reservation={} : {}",
                                orderId, res != null ? res.getReservationReference() : null, e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch reservations for order={}: {}", orderId, e.getMessage());
        }
        // Trigger refund (placeholder)
        refundTriggerService.triggerRefund(updated, actor, reason);

        log.info("CANCELLATION orderId={} actor={} reason={} action=customer_cancel", orderId, actor, reason);
    }

    @Transactional(rollbackFor = Exception.class)
    public void adminCancelOrder(Long orderId, String actor, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        Order updated = lifecycleService.cancelOrder(orderId);

        // Release reservations associated with this order (do not modify inventory
        // directly)
        try {
            java.util.List<InventoryReservation> reservations = reservationService.getReservationsByOrderId(orderId);
            if (reservations != null) {
                for (InventoryReservation res : reservations) {
                    try {
                        if (res != null) {
                            reservationService.releaseReservation(res.getReservationReference());
                        }
                    } catch (Exception e) {
                        log.warn("Failed to release reservation for order={} reservation={} : {}",
                                orderId, res != null ? res.getReservationReference() : null, e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch reservations for order={}: {}", orderId, e.getMessage());
        }
        refundTriggerService.triggerRefund(updated, actor, reason);

        log.info("CANCELLATION orderId={} actor={} reason={} action=admin_cancel", orderId, actor, reason);
    }
}
