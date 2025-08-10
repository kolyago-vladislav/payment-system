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
public class WalletExistenceValidator implements TransactionValidator {

    private final WalletRepository walletRepository;

    @Override
    public void validate(
        ValidationContext context
    ) {
        if (!walletRepository.existsById(UUID.fromString(context.sourceWalletId()))) {
            throw new TransactionServiceException("Wallet not found by id=%s", context.sourceWalletId());
        }

        if (context.targetWalletId() != null && !walletRepository.existsById(UUID.fromString(context.targetWalletId()))) {
            throw new TransactionServiceException("Wallet not found by id=%s", context.sourceWalletId());
        }

    }

    @Override
    public List<TransactionType> getSupportTransactionTypes() {
        return List.of(TransactionType.values());
    }
}
