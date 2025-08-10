package com.example.transaction.business.service.transaction.confirm;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.mapper.ExternalDtoMapper;
import com.example.transaction.business.mapper.OutboxMapper;
import com.example.transaction.business.mapper.TransactionMapper;
import com.example.transaction.business.repository.OutboxRepository;
import com.example.transaction.business.repository.TransactionRepository;
import com.example.transaction.business.repository.WalletRepository;
import com.example.transaction.business.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.business.validation.base.TransactionValidator;
import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.DepositConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.model.dto.ValidationContext;
import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.entity.type.TransactionType;

import static com.example.transaction.model.entity.type.TransactionType.DEPOSIT;

@Component
@RequiredArgsConstructor
public class DepositConfirmHandler implements ConfirmRequestHandler {

    private final TransactionMapper transactionMapper;
    private final OutboxRepository repository;
    private final WalletRepository walletRepository;
    private final OutboxMapper outboxMapper;
    private final JsonWrapper jsonWrapper;
    private final TransactionRepository transactionRepository;
    private final ExternalDtoMapper externalDtoMapper;
    private final List<TransactionValidator> validators;

    @Override
    @Transactional
    public TransactionConfirmResponse handle(ConfirmRequest confirmRequest) {
        var request = (DepositConfirmRequest) confirmRequest;

        validators.stream()
            .filter(validator -> validator.getSupportTransactionTypes().contains(getType()))
            .forEach(validator -> validator.validate(getValidationContext(request)));

        var transaction = transactionRepository.save(transactionMapper.to(request, DEPOSIT));
        fillWallet(request, transaction);

        saveOutboxEvent(transaction);

        return transactionMapper.from(transaction);
    }

    private ValidationContext getValidationContext(DepositConfirmRequest dto) {
        return ValidationContext.builder()
            .sourceWalletId(dto.getWalletId())
            .amount(BigDecimal.valueOf(dto.getAmount()))
            .build();
    }

    private void fillWallet(
        DepositConfirmRequest request,
        Transaction transaction
    ) {
        var wallet = walletRepository.findById(UUID.fromString(request.getWalletId()))
            .orElseThrow(() -> new TransactionServiceException("Wallet not found by id=%s", request.getWalletId()));

        transaction.setWallet(wallet);
    }

    private void saveOutboxEvent(Transaction transaction) {
        var payload = externalDtoMapper.toDepositRequestDto(transaction);
        var outboxEvent = outboxMapper.toDepositEvent(transaction, jsonWrapper.write(payload));

        repository.save(outboxEvent);
    }

    @Override
    public TransactionType getType() {
        return DEPOSIT;
    }
}