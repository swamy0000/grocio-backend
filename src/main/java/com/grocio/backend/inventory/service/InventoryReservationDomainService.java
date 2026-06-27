package com.grocio.backend.inventory.service;

import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.order.entity.Order;

import java.util.List;

public interface InventoryReservationDomainService {

    InventoryReservation reserveInventory(Cart cart);

    InventoryReservation confirmReservation(String reservationReference);

    InventoryReservation releaseReservation(String reservationReference);

    InventoryReservation assignOrderToReservation(String reservationReference, Order order);

    List<InventoryReservation> expireReservations();

    List<InventoryReservation> getReservationsByOrderId(Long orderId);
}
