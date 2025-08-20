package com.example.payment.provider.business.service;

import java.util.random.RandomGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.provider.business.client.WebhookClient;
import com.example.payment.provider.business.repository.TransactionRepository;
import com.example.payment.provider.core.util.DateTimeUtil;
import com.example.payment.provider.dto.StatusUpdateDto;
import com.example.payment.provider.model.dto.Status;
import com.example.payment.provider.model.entity.Transaction;

import static com.example.payment.provider.model.dto.Status.FAILED;
import static com.example.payment.provider.model.dto.Status.SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobTransactionService {

    private final TransactionRepository repository;
    private final WebhookClient webhookClient;
    private final DateTimeUtil dateTimeUtil;

    @Transactional
    public boolean runTransactionJob() {
        return repository.findByPendingStatus()
            .map(this::execute)
            .orElse(false);
    }

    private boolean execute(Transaction transaction) {
        var success = RandomGenerator.getDefault().nextDouble() < 0.65;
        var status = success ? SUCCESS.name() : FAILED.name();

        send(transaction, status);

        log.info("Transaction was executed id={} status={}", transaction.getId(), status);

        return true;
    }

    private void send(
        Transaction transaction,
        String status
    ) {
        try {
            webhookClient.sendTransactionWebhook(new StatusUpdateDto(transaction.getId(), status));
        } catch (Exception e) {
            log.error("Can not send webhook for transaction id={}, retryCount={}", transaction.getId(), transaction.getRetryCount(), e);
            var retryCount = transaction.getRetryCount() + 1;
            transaction
                .setRetryCount(retryCount)
                .setUpdatedAt(dateTimeUtil.createNewTimeToExecute(retryCount));

            if (retryCount > 3) {
                transaction.setStatus(Status.FAILED.name());
            }

            repository.save(transaction);
        }
    }
}
