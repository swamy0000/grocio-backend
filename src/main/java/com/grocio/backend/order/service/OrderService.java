package com.grocio.backend.order.service;

import com.grocio.backend.order.dto.OrderPlacementResponse;
import com.grocio.backend.order.dto.OrderRequest;
import com.grocio.backend.order.entity.Order;

public interface OrderService {

    OrderPlacementResponse placeOrder(OrderRequest request);

    @Deprecated
    OrderPlacementResponse placeOrderWithPendingPayment(OrderRequest request);

    OrderPlacementResponse placePendingOrder(OrderRequest request);

    Order getOrderEntity(Long orderId);
}
