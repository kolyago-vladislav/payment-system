package com.example.transaction.environment.data;

import java.time.Clock;
import java.util.List;

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

import static java.time.ZoneOffset.UTC;

import static com.example.transaction.spec.integration.LifecycleSpecification.USER_ID;

@Component
@RequiredArgsConstructor
public class TransactionDtoCreator {

    private final Clock clock;

    public ConfirmRequest createDepositConfirmTransactionDto(String walletId) {
        return new DepositConfirmRequest()
            .userId(USER_ID)
            .walletId(walletId)
            .amount(100.00)
            .fee(10.00)
            .paymentMethodId(1);
    }

    public ConfirmRequest createWithdrawalConfirmTransactionDto(String walletId) {
        return new DepositConfirmRequest()
            .userId(USER_ID)
            .walletId(walletId)
            .amount(100.00)
            .fee(10.00)
            .paymentMethodId(1);
    }

    public ConfirmRequest createTransferConfirmTransactionDto(String walletId, String targetWalletId) {
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
}
