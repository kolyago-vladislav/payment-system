package com.example.payment.provider.entrypoint.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.provider.api.PayoutApi;
import com.example.payment.provider.business.service.PayoutService;
import com.example.payment.provider.dto.PayoutDto;
import com.example.payment.provider.dto.PayoutWriteDto;

@RestController
@RequiredArgsConstructor
public class PayoutController implements PayoutApi {

    private final PayoutService payoutService;

    @Override
    public ResponseEntity<PayoutDto> createPayout(PayoutWriteDto payoutWriteDto) {
        return ResponseEntity.ok(payoutService.createPayout(payoutWriteDto));
    }

    @Override
    public ResponseEntity<PayoutDto> findById(Long id) {
        return ResponseEntity.ok(payoutService.findByPayoutId(id));
    }
}
