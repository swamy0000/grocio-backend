package com.grocio.backend.checkout;

import java.math.BigDecimal;

import com.grocio.backend.address.entity.Address;
import com.grocio.backend.cart.entity.Cart;
import com.grocio.backend.financial.payment.dto.PaymentResponse;
import com.grocio.backend.inventory.entity.InventoryReservation;
import com.grocio.backend.order.entity.Order;
import com.grocio.backend.checkout.dto.CheckoutRequest;
import com.grocio.backend.coupon.dto.CouponValidationResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutContext {

    private CheckoutRequest request;
    private Cart cart;
    private Address address;
    private CouponValidationResult couponValidationResult;
    private BigDecimal finalPayableAmount;
    private InventoryReservation inventoryReservation;
    private Order order;
    private PaymentResponse paymentResponse;
}
