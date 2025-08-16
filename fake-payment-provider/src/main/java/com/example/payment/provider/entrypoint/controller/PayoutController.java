package com.example.payment.provider.entrypoint.controller;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.api.PayoutApi;
import com.example.payment.dto.Payout;
import com.example.payment.dto.PayoutRequest;

@RestController
@RequiredArgsConstructor
public class PayoutController implements PayoutApi {

    @Override
    public ResponseEntity<Payout> createPayout(PayoutRequest payoutRequest) {
        return null;
    }

    @Override
    public ResponseEntity<Payout> findById(Integer id) {
        return null;
    }

    @Override
    public ResponseEntity<List<Payout>> findPayouts(
        OffsetDateTime startDate,
        OffsetDateTime endDate
    ) {
        return null;
    }
}
