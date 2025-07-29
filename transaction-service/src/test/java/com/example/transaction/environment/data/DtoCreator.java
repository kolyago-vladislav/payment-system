package com.example.transaction.environment.data;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.DepositConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionDto;
import com.example.transaction.dto.TransactionPageDto;
import com.example.transaction.dto.TransactionStatusDto;
import com.example.transaction.dto.TransactionTypeDto;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.dto.WalletDto;
import com.example.transaction.dto.WalletPageDto;
import com.example.transaction.dto.WalletStatusDto;
import com.example.transaction.dto.WalletWriteDto;
import com.example.transaction.model.dto.DepositRequestDto;
import com.example.transaction.model.dto.WithdrawalRequestDto;
import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.model.entity.type.TransactionType;

import static java.time.ZoneOffset.UTC;

import static com.example.transaction.spec.integration.LifecycleSpecification.USER_ID;

@Component
@RequiredArgsConstructor
public class DtoCreator {
    private final JsonWrapper jsonWrapper;
    private final Clock clock;

    public WalletWriteDto createWalletWriteDto() {
        return new WalletWriteDto()
            .name("My_name")
            .userId(USER_ID)
            .walletTypeId("00000000-1111-2222-3333-000000000001");
    }

    public WalletPageDto createWalletPageDto(String id) {
        return new WalletPageDto()
            .items(List.of(createWalletDto(id)));
    }

    public WalletDto createWalletDto(String id) {
        return new WalletDto()
            .id(id)
            .name("My_name")
            .walletType("Main Wallet")
            .currency("USD")
            .userId(USER_ID)
            .status(WalletStatusDto.ACTIVE)
            .balance(0.0);
    }

    public ConfirmRequest createDepositConfirmDto(String walletId) {
        return new DepositConfirmRequest()
            .userId(USER_ID)
            .walletId(walletId)
            .amount(100.00)
            .fee(10.00)
            .paymentMethodId(1);
    }

    public ConfirmRequest createWithdrawalConfirmDto(String walletId) {
        return new DepositConfirmRequest()
            .userId(USER_ID)
            .walletId(walletId)
            .amount(100.00)
            .fee(10.00)
            .paymentMethodId(1);
    }

    public ConfirmRequest createTransferConfirmDto(String walletId, String targetWalletId) {
        return new TransferConfirmRequest()
            .userId(USER_ID)
            .fromWalletId(walletId)
            .toWalletId(targetWalletId)
            .amount(100.00)
            .fee(10.00);
    }

    public TransactionPageDto createTransactionPageDto(String id, String id2) {
        return new TransactionPageDto()
            .count(1)
            .items(List.of(createTransactionDto(id), createTransactionDto(id2)));
    }

    public TransactionDto createTransactionDto(String transactionId) {
        return new TransactionDto()
            .transactionId(transactionId)
            .status(TransactionStatusDto.PENDING)
            .amount(100.00)
            .currency("USD")
            .type(TransactionTypeDto.DEPOSIT)
            .updatedAt(clock.instant().atOffset(UTC))
            .createdAt(clock.instant().atOffset(UTC));
    }

    public TransactionConfirmResponse createTransactionConfirmResponse(String id) {
        return new TransactionConfirmResponse()
            .transactionId(id)
            .status(TransactionStatusDto.PENDING);
    }

    public OutboxEvent createDepositOutboxEvent(String walletId, String transactionId, String traceId) {
        var depositRequest = getDepositRequest(walletId, transactionId);
        var payload = jsonWrapper.write(depositRequest);
        return createOutboxEvent(transactionId, traceId, TransactionType.DEPOSIT, payload);
    }

    public OutboxEvent createWithdrawalOutboxEvent(String walletId, String transactionId, String traceId) {
        var withdrawalRequest = getWithdrawalRequest(walletId, transactionId);
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

    public WithdrawalRequestDto getWithdrawalRequest(
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

    public DepositRequestDto getDepositRequest(
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
