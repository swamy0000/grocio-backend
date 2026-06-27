package com.grocio.backend.checkout.internal;

import java.math.BigDecimal;

import com.grocio.backend.address.entity.Address;
import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.checkout.dto.CheckoutRequest;
import com.grocio.backend.coupon.dto.CouponValidationResult;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.financial.payment.entity.Payment;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.order.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutContext {

    private CheckoutRequest request;

    private Cart cart;

    private Address address;

    private Order order;

    private InventoryReservation inventoryReservation;

    private Payment payment;

    private PaymentResponse paymentResponse;

    private CouponValidationResult couponValidationResult;

    private BigDecimal finalPayableAmount = BigDecimal.ZERO;

    private boolean paymentRequired = true;
}