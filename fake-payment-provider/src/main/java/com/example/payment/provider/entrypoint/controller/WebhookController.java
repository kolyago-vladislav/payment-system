package com.example.payment.provider.entrypoint.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.api.WebhookApi;
import com.example.payment.dto.StatusUpdate;

@RestController
@RequiredArgsConstructor
public class WebhookController implements WebhookApi {

    @Override
    public ResponseEntity<Void> registerTransactionWebhook(StatusUpdate statusUpdate) {
        return null;
    }

    @Override
    public ResponseEntity<Void> registerWithdrawalWebhook(StatusUpdate statusUpdate) {
        return null;
    }
}
