package com.example.payment.provider.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.provider.business.repository.WebhookRepository;
import com.example.payment.provider.core.util.DateTimeUtil;
import com.example.payment.provider.core.util.JsonWrapper;
import com.example.payment.provider.dto.StatusUpdateDto;
import com.example.payment.provider.model.entity.Webhook;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final TransactionService transactionService;
    private final PayoutService payoutService;
    private final WebhookRepository repository;
    private final JsonWrapper jsonWrapper;
    private final DateTimeUtil dateTimeUtil;

    @Transactional
    public void updateTransactionStatus(StatusUpdateDto statusUpdateDto) {
        transactionService.updateStatus(statusUpdateDto);

        var received = createWebhook("TRANSACTION", statusUpdateDto);

        repository.save(received);
    }

    @Transactional
    public void updateWithdrawalStatus(StatusUpdateDto statusUpdateDto) {
        payoutService.updateStatus(statusUpdateDto);

        var received = createWebhook("PAYOUT", statusUpdateDto);

        repository.save(received);
    }

    private Webhook createWebhook(
        String type,
        StatusUpdateDto statusUpdateDto
    ) {
        return new Webhook()
            .setEventType(type)
            .setEntityId(statusUpdateDto.getId())
            .setPayload(jsonWrapper.write(statusUpdateDto))
            .setReceivedAt(dateTimeUtil.now());
    }
}
