package com.example.enivronment.config.testcontainer.data;

import java.time.Clock;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.individual.dto.ConfirmRequest;
import com.example.individual.dto.DepositConfirmRequest;
import com.example.individual.dto.TransactionConfirmResponse;
import com.example.individual.dto.TransactionDto;
import com.example.individual.dto.TransactionPageDto;
import com.example.individual.dto.TransactionStatusDto;
import com.example.individual.dto.TransactionTypeDto;
import com.example.individual.dto.TransferConfirmRequest;
import com.example.individual.dto.WithdrawalConfirmRequest;

import static java.time.ZoneOffset.UTC;

import static com.example.spec.integration.LifecycleSpecification.PERSON_ID;
import static com.example.spec.integration.LifecycleSpecification.TRANSACTION_ID;
import static com.example.spec.integration.LifecycleSpecification.WALLET_ID;

@Component
@RequiredArgsConstructor
public class TransactionDtoCreator {

    private final Clock clock;

    public ConfirmRequest createDepositConfirmTransactionDto() {
        return new DepositConfirmRequest()
            .userId(PERSON_ID)
            .walletId(WALLET_ID)
            .amount(100.00)
            .fee(10.00)
            .paymentMethodId(1);
    }

    public ConfirmRequest createWithdrawalConfirmTransactionDto() {
        return new WithdrawalConfirmRequest()
            .userId(PERSON_ID)
            .walletId(WALLET_ID)
            .amount(100.00)
            .fee(10.00)
            .paymentMethodId(1);
    }

    public ConfirmRequest createTransferConfirmTransactionDto() {
        return new TransferConfirmRequest()
            .userId(PERSON_ID)
            .fromWalletId(WALLET_ID)
            .toWalletId(WALLET_ID)
            .amount(100.00)
            .fee(10.00);
    }

    public TransactionPageDto createTransactionPageDto(String id) {
        return new TransactionPageDto()
            .count(1)
            .items(List.of(createTransactionDto(id)));
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

    public TransactionConfirmResponse createTransactionConfirmResponse() {
        return new TransactionConfirmResponse()
            .transactionId(TRANSACTION_ID)
            .status(TransactionStatusDto.PENDING);
    }
}
