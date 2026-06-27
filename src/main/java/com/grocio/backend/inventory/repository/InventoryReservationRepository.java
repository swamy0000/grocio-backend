package com.grocio.backend.inventory.repository;

import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.inventory.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
    Optional<InventoryReservation> findByReservationReference(String reservationReference);

    List<InventoryReservation> findByStatus(ReservationStatus status);

    List<InventoryReservation> findByOrder_OrderId(Long orderId);

    List<InventoryReservation> findByExpiresAtBefore(LocalDateTime now);
}
