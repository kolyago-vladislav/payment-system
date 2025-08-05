package com.example.transaction.business.service.transaction.confirm;

import java.math.BigDecimal;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.mapper.TransactionMapper;
import com.example.transaction.business.repository.TransactionRepository;
import com.example.transaction.business.service.WalletService;
import com.example.transaction.business.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.business.validation.base.TransactionValidator;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.model.dto.ValidationContext;
import com.example.transaction.model.entity.type.TransactionType;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

import static com.example.transaction.model.entity.type.TransactionType.TRANSFER;

@Component
@RequiredArgsConstructor
public class TransferConfirmHandler implements ConfirmRequestHandler {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final List<TransactionValidator> validators;

    @Override
    @Transactional(isolation = REPEATABLE_READ)
    public TransactionConfirmResponse handle(ConfirmRequest confirmRequest) {
        var request = (TransferConfirmRequest) confirmRequest;

        validators.stream()
            .filter(validator -> validator.getSupportTransactionTypes().contains(getType()))
            .forEach(validator -> validator.validate(getValidationContext(request)));

        var transaction = transactionRepository.save(transactionMapper.to(request, TRANSFER));
        walletService.debit(transaction.getWalletId(), transaction.getAmount());
        walletService.credit(transaction.getTargetWalletId(), transaction.getAmount());

        return transactionMapper.from(transaction);
    }

    private ValidationContext getValidationContext(TransferConfirmRequest dto) {
        return ValidationContext.builder()
            .initialWalletId(dto.getFromWalletId())
            .targetWalletId(dto.getToWalletId())
            .amount(BigDecimal.valueOf(dto.getAmount()))
            .build();
    }

    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }
}