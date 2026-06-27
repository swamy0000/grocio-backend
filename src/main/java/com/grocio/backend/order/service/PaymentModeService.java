package com.grocio.backend.order.service;

import com.grocio.backend.repository.PaymentModeRepository1;
import com.grocio.backend.order.dto.PaymentModeResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentModeService {

    private final PaymentModeRepository1 paymentModeRepository;

    public List<PaymentModeResponse> getActivePaymentModes() {
        return paymentModeRepository.findByEnabledTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private PaymentModeResponse toResponse(com.grocio.backend.entity.PaymentMode1 paymentModeEntity) {
        PaymentModeResponse response = new PaymentModeResponse();
        response.setId(paymentModeEntity.getId());
        response.setCode(paymentModeEntity.getCode());
        response.setName(paymentModeEntity.getName());
        response.setIcon(paymentModeEntity.getIcon());
        response.setDisplayOrder(paymentModeEntity.getDisplayOrder());
        response.setEnabled(paymentModeEntity.getEnabled());
        response.setComingSoon(paymentModeEntity.getComingSoon());
        return response;
    }
}
