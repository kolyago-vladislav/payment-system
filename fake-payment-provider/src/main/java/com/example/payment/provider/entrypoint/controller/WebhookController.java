package com.example.payment.provider.entrypoint.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.payment.provider.api.WebhookApi;
import com.example.payment.provider.business.service.WebhookService;
import com.example.payment.provider.dto.StatusUpdateDto;

@RestController
@RequiredArgsConstructor
public class WebhookController implements WebhookApi {

    private final WebhookService webhookService;

    @Override
    public ResponseEntity<Void> registerTransactionWebhook(StatusUpdateDto statusUpdateDto) {
        webhookService.updateTransactionStatus(statusUpdateDto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> registerWithdrawalWebhook(StatusUpdateDto statusUpdateDto) {
        webhookService.updateWithdrawalStatus(statusUpdateDto);
        return ResponseEntity.ok().build();
    }
}
