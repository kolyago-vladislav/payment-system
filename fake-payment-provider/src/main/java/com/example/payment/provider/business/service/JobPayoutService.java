package com.example.payment.provider.business.service;

import java.util.random.RandomGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.provider.business.client.WebhookClient;
import com.example.payment.provider.business.repository.PayoutRepository;
import com.example.payment.provider.core.config.property.BackoffProperty;
import com.example.payment.provider.core.util.DateTimeUtil;
import com.example.payment.provider.dto.StatusUpdateDto;
import com.example.payment.provider.model.dto.Status;
import com.example.payment.provider.model.entity.Payout;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPayoutService {

    private final PayoutRepository repository;
    private final WebhookClient webhookClient;
    private final DateTimeUtil dateTimeUtil;
    private final BackoffProperty backoffProperty;

    @Transactional
    public boolean runPayoutJob() {
        log.info("Try to execute payout job");
        return repository.findByPendingStatus()
            .map(this::execute)
            .orElse(false);
    }

    private boolean execute(Payout payout) {
        var success = RandomGenerator.getDefault().nextDouble() < backoffProperty.failureRateThreshold();
        var status = success ? Status.SUCCESS.name() : Status.FAILED.name();

        send(payout, status);

        log.info("Payout was executed id={} status={}", payout.getId(), status);

        return true;
    }

    private void send(
        Payout payout,
        String status
    ) {
        try {
            webhookClient.sendPayoutWebhook(new StatusUpdateDto(payout.getId(), status));
        } catch (Exception e) {
            log.error("Can not send webhook for payout id={}, retryCount={}", payout.getId(), payout.getRetryCount(), e);
            var retryCount = payout.getRetryCount() + 1;
            payout
                .setRetryCount(retryCount)
                .setUpdatedAt(dateTimeUtil.createNewTimeToExecute(retryCount));

            if (retryCount > backoffProperty.maxRetryCount()) {
                payout.setStatus(Status.FAILED.name());
            }

            repository.save(payout);
        }
    }
}
