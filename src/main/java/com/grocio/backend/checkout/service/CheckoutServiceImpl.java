package com.grocio.backend.checkout.service;

import com.grocio.backend.checkout.dto.CheckoutRequest;
import com.grocio.backend.checkout.dto.CheckoutResponse;
import com.grocio.backend.checkout.internal.CheckoutContext;
import com.grocio.backend.checkout.internal.CheckoutOrchestrator;
import com.grocio.backend.checkout.mapper.CheckoutMapper;
import com.grocio.backend.checkout.validator.CheckoutValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutValidator checkoutValidator;
    private final CheckoutOrchestrator checkoutOrchestrator;
    private final CheckoutMapper checkoutMapper;

    @Override
    public CheckoutResponse checkout(CheckoutRequest request) {
        checkoutValidator.validate(request);
        CheckoutContext context = checkoutOrchestrator.process(request);
        return checkoutMapper.toResponse(context);
    }
}
