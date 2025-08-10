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
public class WalletCurrencyValidator implements TransactionValidator {

    private final WalletRepository walletRepository;

    @Override
    public void validate(
        ValidationContext context
    ) {
        var sourceWallet = walletRepository.findById(UUID.fromString(context.sourceWalletId()))
            .orElseThrow(() -> new TransactionServiceException("Wallet not found by id=%s", context.sourceWalletId()));
        var targetWallet = walletRepository.findById(UUID.fromString(context.targetWalletId()))
            .orElseThrow(() -> new TransactionServiceException("Wallet not found by id=%s", context.targetWalletId()));

        validate(sourceWallet.getWalletType().getCurrencyCode(), context.sourceCurrency(), sourceWallet.getId());
        validate(targetWallet.getWalletType().getCurrencyCode(), context.targetCurrency(), targetWallet.getId());
    }

    private static void validate(
        String walletCurrencyCode,
        String expectedCurrencyCode,
        UUID walletId
    ) {
        if (!walletCurrencyCode.equals(expectedCurrencyCode)) {
            throw new TransactionServiceException(
                "Wallet currency is not matched wallet id=%s: currency=%s, rateCurrency=%s",
                walletId,
                walletCurrencyCode,
                expectedCurrencyCode
            );
        }
    }

    @Override
    public List<TransactionType> getSupportTransactionTypes() {
        return List.of(TransactionType.TRANSFER);
    }
}
