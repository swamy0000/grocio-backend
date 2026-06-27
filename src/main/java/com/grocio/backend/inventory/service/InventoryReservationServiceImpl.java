package com.grocio.backend.inventory.service;

import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.cart.entity.CartItem;
import com.grocio.backend.inventory.entity.Inventory;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.inventory.entity.InventoryReservationItem;
import com.grocio.backend.inventory.enums.ReservationStatus;
import com.grocio.backend.inventory.exception.InventoryException;
import com.grocio.backend.inventory.internal.ReservationReferenceGenerator;
import com.grocio.backend.inventory.repository.InventoryRepository;
import com.grocio.backend.inventory.repository.InventoryReservationItemRepository;
import com.grocio.backend.inventory.repository.InventoryReservationRepository;
import com.grocio.backend.order.entity.Order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import jakarta.persistence.OptimisticLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryReservationServiceImpl implements InventoryReservationService {

    private final InventoryRepository inventoryRepository;
    private final InventoryReservationRepository inventoryReservationRepository;
    private final InventoryReservationItemRepository reservationItemRepository;
    private final ReservationReferenceGenerator referenceGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryReservation reserveInventory(Cart cart) {
        /**
         * Create a reservation and decrement sellable stock atomically.
         * Uses optimistic locking on Inventory to detect concurrent updates.
         */
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new InventoryException("Cart must contain items to create a reservation.");
        }

        InventoryReservation reservation = new InventoryReservation();
        reservation.setReservationReference(referenceGenerator.generateReference());
        reservation.setUserId(cart.getUserId());
        reservation.setOrder(null);
        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        List<InventoryReservationItem> reservationItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            if (item.getProduct() == null) {
                throw new InventoryException("Cart item must contain a product.");
            }
            System.out.println("In reserveInventory:: Cart Product ID = " + item.getProduct().getProductId());
            Inventory inventory = inventoryRepository.findByProduct_ProductId(
                    item.getProduct().getProductId())
                    .orElseThrow(() -> new InventoryException(
                            "Inventory not found for product " + item.getProduct().getProductId()));

            // Use Inventory helper methods (which throw InventoryException on business rule
            // violations)
            try {
                if (!inventory.hasAvailableStock(item.getQuantity())) {
                    throw new InventoryException("Insufficient stock for product " + item.getProduct().getProductId());
                }

                inventory.reserveStock(item.getQuantity());
                inventoryRepository.save(inventory);
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
                throw new InventoryException("Concurrent update detected while reserving product " +
                        item.getProduct().getProductId() + ". Please retry.", ex);
            }

            InventoryReservationItem reservationItem = new InventoryReservationItem();
            reservationItem.setReservation(reservation);
            reservationItem.setProduct(item.getProduct());
            reservationItem.setQuantity(item.getQuantity());
            reservationItems.add(reservationItem);
        }

        reservation.setItems(reservationItems);
        // Saving reservation cascades items (CascadeType.ALL). Avoid duplicate saves.
        return inventoryReservationRepository.save(reservation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryReservation confirmReservation(String reservationReference) {
        /**
         * Confirm a previously RESERVED reservation. Validates status transition and
         * applies inventory confirmation using optimistic locking semantics.
         */
        InventoryReservation reservation = findReservedReservation(reservationReference);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        for (InventoryReservationItem item : reservation.getItems()) {
            System.out.println("In confirmReservation:: Cart Product ID = " + item.getProduct().getProductId());
            Inventory inventory = inventoryRepository.findByProduct_ProductId(
                    item.getProduct().getProductId())
                    .orElseThrow(() -> new InventoryException(
                            "Inventory not found for product " + item.getProduct().getProductId()));
            try {
                inventory.confirmReservation(item.getQuantity());
                inventoryRepository.save(inventory);
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
                throw new InventoryException("Concurrent update detected while confirming reservation for product " +
                        item.getProduct().getProductId() + ". Please retry.", ex);
            }
        }

        return inventoryReservationRepository.save(reservation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryReservation releaseReservation(String reservationReference) {
        /**
         * Release a previously RESERVED reservation. Validates status transition and
         * restores reserved quantity to inventory.
         */
        InventoryReservation reservation = findReservedReservation(reservationReference);
        reservation.setStatus(ReservationStatus.RELEASED);

        for (InventoryReservationItem item : reservation.getItems()) {
            System.out.println("In releaseReservation:: Cart Product ID = " + item.getProduct().getProductId());
            Inventory inventory = inventoryRepository.findByProduct_ProductId(
                    item.getProduct().getProductId())
                    .orElseThrow(() -> new InventoryException(
                            "Inventory not found for product " + item.getProduct().getProductId()));
            try {
                inventory.releaseReservedQuantity(item.getQuantity());
                inventoryRepository.save(inventory);
            } catch (OptimisticLockException | ObjectOptimisticLockingFailureException ex) {
                throw new InventoryException("Concurrent update detected while releasing reservation for product " +
                        item.getProduct().getProductId() + ". Please retry.", ex);
            }
        }

        return inventoryReservationRepository.save(reservation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryReservation assignOrderToReservation(String reservationReference, Order order) {
        InventoryReservation reservation = inventoryReservationRepository
                .findByReservationReference(reservationReference)
                .orElseThrow(() -> new InventoryException("Reservation not found: " + reservationReference));

        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new InventoryException("Reservation is not in RESERVED state: " + reservationReference);
        }

        reservation.setOrder(order);
        return inventoryReservationRepository.save(reservation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<InventoryReservation> expireReservations() {
        List<InventoryReservation> expiredReservations = inventoryReservationRepository
                .findByExpiresAtBefore(LocalDateTime.now());
        List<InventoryReservation> result = new ArrayList<>();

        for (InventoryReservation reservation : expiredReservations) {
            // Only attempt to release reservations that are still RESERVED.
            // releaseReservation will
            // validate the transition; skip otherwise to avoid double-processing.
            if (reservation.getStatus() == ReservationStatus.RESERVED) {
                result.add(releaseReservation(reservation.getReservationReference()));
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<InventoryReservation> getReservationsByOrderId(Long orderId) {
        return inventoryReservationRepository.findByOrder_OrderId(orderId);
    }

    private InventoryReservation findReservedReservation(String reservationReference) {
        InventoryReservation reservation = inventoryReservationRepository
                .findByReservationReference(reservationReference)
                .orElseThrow(() -> new InventoryException("Reservation not found: " + reservationReference));

        if (reservation.getStatus() != ReservationStatus.RESERVED) {
            throw new InventoryException("Reservation is not in RESERVED state: " + reservationReference);
        }

        return reservation;
    }

}
