package com.example.transaction.environment.data;

import java.time.Clock;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.model.dto.DepositRequestDto;
import com.example.transaction.model.dto.WithdrawalRequestDto;
import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.model.entity.type.TransactionType;

import static com.example.transaction.spec.integration.LifecycleSpecification.USER_ID;

@Component
@RequiredArgsConstructor
public class OutboxEventDtoCreator {

    private final JsonWrapper jsonWrapper;
    private final Clock clock;

    public OutboxEvent createDepositOutboxEvent(String walletId, String transactionId, String traceId) {
        var depositRequest = getDepositRequestOutboxEvent(walletId, transactionId);
        var payload = jsonWrapper.write(depositRequest);
        return createOutboxEvent(transactionId, traceId, TransactionType.DEPOSIT, payload);
    }

    public OutboxEvent createWithdrawalOutboxEvent(String walletId, String transactionId, String traceId) {
        var withdrawalRequest = getWithdrawalRequestOutboxEvent(walletId, transactionId);
        var payload = jsonWrapper.write(withdrawalRequest);
        return createOutboxEvent(transactionId, traceId, TransactionType.WITHDRAWAL, payload);
    }

    private OutboxEvent createOutboxEvent(
        String transactionId,
        String traceId,
        TransactionType transactionType,
        String payload
    ) {
        return new OutboxEvent()
            .setCreatedAt(clock.instant())
            .setUserId(UUID.fromString(USER_ID))
            .setTransactionId(UUID.fromString(transactionId))
            .setTraceId(traceId)
            .setType(transactionType)
            .setPayload(payload);
    }

    public WithdrawalRequestDto getWithdrawalRequestOutboxEvent(
        String walletId,
        String transactionId
    ) {
        return WithdrawalRequestDto.builder()
            .amount(100.00)
            .userId(UUID.fromString(USER_ID))
            .currency("USD")
            .walletId(UUID.fromString(walletId))
            .timestamp(clock.instant())
            .transactionId(UUID.fromString(transactionId))
            .build();
    }

    public DepositRequestDto getDepositRequestOutboxEvent(
        String walletId,
        String transactionId
    ) {
        return DepositRequestDto.builder()
            .amount(100.00)
            .userId(UUID.fromString(USER_ID))
            .currency("USD")
            .walletId(UUID.fromString(walletId))
            .timestamp(clock.instant())
            .transactionId(UUID.fromString(transactionId))
            .build();
    }
}
