package com.grocio.backend.inventory.entity;

import com.grocio.backend.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import com.grocio.backend.inventory.exception.InventoryException;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory", indexes = {
        @Index(name = "idx_inventory_product_id", columnList = "product_id")
})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Min(0)
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity = 0;

    @Min(0)
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Version
    @Column(nullable = false)
    private Long version;

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

    public Integer getSellableQuantity() {
        return availableQuantity - reservedQuantity;
    }

    public boolean hasAvailableStock(Integer requestedQuantity) {
        return requestedQuantity != null && getSellableQuantity() >= requestedQuantity;
    }

    public void reserveStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("Reservation quantity must be positive.");
        }
        if (!hasAvailableStock(quantity)) {
            throw new InventoryException("Insufficient sellable stock to reserve.");
        }
        reservedQuantity += quantity;
    }

    public void confirmReservation(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("Confirmation quantity must be positive.");
        }
        if (reservedQuantity < quantity) {
            throw new InventoryException("Cannot confirm more than reserved quantity.");
        }
        if (availableQuantity < quantity) {
            throw new InventoryException("Insufficient available quantity to confirm reservation.");
        }
        reservedQuantity -= quantity;
        availableQuantity -= quantity;
    }

    public void releaseReservedQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InventoryException("Release quantity must be positive.");
        }
        if (reservedQuantity < quantity) {
            throw new InventoryException("Cannot release more than reserved quantity.");
        }
        reservedQuantity -= quantity;
    }
}
