package com.grocio.backend.inventory.entity;

import com.grocio.backend.order.entity.Order;
import com.grocio.backend.inventory.enums.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_reservations", indexes = {
        @Index(name = "idx_inventory_reservation_reference", columnList = "reservation_reference"),
        @Index(name = "idx_inventory_reservation_status", columnList = "status"),
        @Index(name = "idx_inventory_reservation_user_id", columnList = "user_id"),
        @Index(name = "idx_inventory_reservation_order_id", columnList = "order_id"),
        @Index(name = "idx_inventory_reservation_expires_at", columnList = "expires_at")
})
public class InventoryReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @NotBlank
    @Column(name = "reservation_reference", nullable = false, unique = true, length = 32)
    private String reservationReference;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ReservationStatus status;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Version
    @Column(nullable = false)
    private Long version;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<InventoryReservationItem> items = new java.util.ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
