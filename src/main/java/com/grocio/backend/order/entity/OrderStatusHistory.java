package com.grocio.backend.order.entity;

import com.grocio.backend.order.lifecycle.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "order_status_history",
    indexes = {
        @Index(name = "idx_order_history_order", columnList = "order_id"),
        @Index(name = "idx_order_history_changed", columnList = "changed_at")
    }
)
@Data
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus toStatus;

    @Column(name = "actor")
    private String actor;

    @Column(name = "changed_at")
    private LocalDateTime changedAt = LocalDateTime.now();

    private String remarks;
}
