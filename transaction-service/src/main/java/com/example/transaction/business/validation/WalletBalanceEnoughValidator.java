package com.example.transaction.business.validation;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.business.repository.WalletRepository;
import com.example.transaction.business.validation.base.TransactionValidator;
import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.model.dto.ValidationContext;
import com.example.transaction.model.entity.type.TransactionType;

@Component
@RequiredArgsConstructor
public class WalletBalanceEnoughValidator implements TransactionValidator {

    private final WalletRepository walletRepository;

    @Override
    public void validate(
        ValidationContext context
    ) {
        var wallet = walletRepository.findById(UUID.fromString(context.initialWalletId()))
            .orElseThrow(() -> new TransactionServiceException("Wallet not found by id=%s", context.initialWalletId()));
        if (wallet.getBalance().compareTo(context.amount()) < 0) {
            throw new TransactionServiceException(
                "Insufficient funds in wallet id=%s: balance=%s, required=%s",
                context.initialWalletId(),
                wallet.getBalance(),
                context.amount()
            );
        }
    }

    @Override
    public List<TransactionType> getSupportTransactionTypes() {
        return List.of(TransactionType.TRANSFER, TransactionType.WITHDRAWAL);
    }
}
