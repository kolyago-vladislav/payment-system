package com.example.transaction.business.service.transaction;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.repository.TransactionRepository;
import com.example.transaction.business.service.WalletService;
import com.example.transaction.business.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.business.service.transaction.init.base.InitRequestHandler;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionTypeDto;
import com.example.transaction.model.dto.DepositCompletedDto;
import com.example.transaction.model.dto.WithdrawalCompletedDto;
import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.entity.type.TransactionStatus;
import com.example.transaction.model.entity.type.TransactionType;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final Map<TransactionType, InitRequestHandler> initRequestHandlers;
    private final Map<TransactionType, ConfirmRequestHandler> confirmRequestHandlers;
    private final WalletService walletService;
    private final TransactionRepository repository;

    public TransactionInitResponse init(
        TransactionTypeDto type,
        InitRequest initRequest
    ) {
        return initRequestHandlers.get(TransactionType.from(type)).handle(initRequest);
    }

    @Transactional
    public TransactionConfirmResponse confirm(
        TransactionTypeDto type,
        ConfirmRequest confirmRequest
    ) {
        return confirmRequestHandlers.get(TransactionType.from(type)).handle(confirmRequest);
    }

    @Transactional(isolation = REPEATABLE_READ)
    public void processDepositCompleteEvent(DepositCompletedDto dto) {
        tryGetTransactionWithLock(dto.transactionId())
            .ifPresent(transaction -> {
                switch (dto.status()) {
                    case FAILED -> failDepositTransaction(transaction);
                    case COMPLETED -> completeDepositTransaction(transaction);
                }
            });
    }

    private Optional<Transaction> tryGetTransactionWithLock(UUID transactionId) {
        try {
            return repository.findByIdForUpdate(transactionId)
                .filter(tx -> tx.getStatus() == TransactionStatus.PENDING);
        } catch (CannotAcquireLockException e) {
            log.warn("Cannot acquire lock for transaction: {}", transactionId);
            return tryGetTransactionWithLock(transactionId);
        }
    }

    private void failDepositTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.FAILED);
        repository.save(transaction);
    }

    private void completeDepositTransaction(Transaction transaction) {
        walletService.credit(transaction);
        transaction.setStatus(TransactionStatus.COMPLETED);
        repository.save(transaction);
    }

    @Transactional(isolation = REPEATABLE_READ)
    public void processWithdrawalCompleteEvent(WithdrawalCompletedDto dto) {
        tryGetTransactionWithLock(dto.transactionId())
            .ifPresent(transaction -> {
                switch (dto.status()) {
                    case FAILED -> failWithdrawalTransaction(transaction);
                    case COMPLETED -> completeWithdrawalTransaction(transaction);
                }
            });
    }

    private void failWithdrawalTransaction(Transaction transaction) {
        walletService.credit(transaction);
        transaction.setStatus(TransactionStatus.FAILED);
        repository.save(transaction);
    }

    private void completeWithdrawalTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.COMPLETED);
        repository.save(transaction);
    }
}
