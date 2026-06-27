package com.grocio.backend.inventory.repository;

import com.grocio.backend.inventory.entity.InventoryReservationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryReservationItemRepository extends JpaRepository<InventoryReservationItem, Long> {
    List<InventoryReservationItem> findByReservation_ReservationId(Long reservationId);

    List<InventoryReservationItem> findByProduct_ProductId(Long productId);
}
