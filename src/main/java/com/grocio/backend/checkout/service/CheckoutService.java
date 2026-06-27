package com.grocio.backend.checkout.service;

import com.grocio.backend.checkout.dto.CheckoutRequest;
import com.grocio.backend.checkout.dto.CheckoutResponse;

public interface CheckoutService {

    CheckoutResponse checkout(CheckoutRequest request);
}
