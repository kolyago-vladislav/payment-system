package com.example.transaction.business.service.transaction;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.business.mapper.TransactionMapper;
import com.example.transaction.business.repository.TransactionRepository;
import com.example.transaction.business.service.WalletService;
import com.example.transaction.business.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.business.service.transaction.init.base.InitRequestHandler;
import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionDto;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionPageDto;
import com.example.transaction.dto.TransactionTypeDto;
import com.example.transaction.model.dto.DepositCompletedDto;
import com.example.transaction.model.dto.TransactionPageRequestDto;
import com.example.transaction.model.dto.WithdrawalCompletedDto;
import com.example.transaction.model.entity.Transaction;
import com.example.transaction.model.entity.type.TransactionStatus;
import com.example.transaction.model.entity.type.TransactionType;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

import static com.example.transaction.core.util.CollectionSqlQueryConverterUtil.toArray;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final Map<TransactionType, InitRequestHandler> initRequestHandlers;
    private final Map<TransactionType, ConfirmRequestHandler> confirmRequestHandlers;
    private final WalletService walletService;
    private final TransactionRepository repository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    public TransactionInitResponse init(
        TransactionTypeDto type,
        InitRequest initRequest
    ) {
        return initRequestHandlers.get(TransactionType.from(type)).handle(initRequest);
    }

    @WithSpan("TransactionService.confirm")
    @Transactional
    public TransactionConfirmResponse confirm(
        TransactionTypeDto type,
        ConfirmRequest confirmRequest
    ) {
        return confirmRequestHandlers.get(TransactionType.from(type)).handle(confirmRequest);
    }

    @WithSpan("TransactionService.processDepositCompleteEvent")
    @Transactional(isolation = REPEATABLE_READ)
    public void processDepositCompleteEvent(DepositCompletedDto dto) {
        tryGetTransactionWithLock(dto.transactionId())
            .ifPresent(transaction -> {
                log.debug("Locking transaction is successfully done in database: {}", transaction.getId());
                switch (dto.status()) {
                    case FAILED -> failDepositTransaction(transaction);
                    case COMPLETED -> completeDepositTransaction(transaction);
                }
            });
    }

    private Optional<Transaction> tryGetTransactionWithLock(UUID transactionId) {
        try {
            var transaction = repository.findByIdForUpdate(transactionId);

            if (transaction.isEmpty()) {
                log.warn("Duplicated execution of transaction with id={} was skipped", transactionId);
            }

            return transaction;
        } catch (CannotAcquireLockException e) {
            log.warn("Cannot acquire lock for transaction: {}", transactionId);
            return tryGetTransactionWithLock(transactionId);
        }
    }

    private void failDepositTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.FAILED);
        repository.save(transaction);
        log.error("Deposit transaction failed: {}", transaction.getId());
    }

    private void completeDepositTransaction(Transaction transaction) {
        walletService.credit(transaction.getWallet().getId(), transaction.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);
        repository.save(transaction);
        log.info("Deposit transaction successfully executed: {}", transaction.getId());
    }

    @Transactional(isolation = REPEATABLE_READ)
    public void processWithdrawalCompleteEvent(WithdrawalCompletedDto dto) {
        tryGetTransactionWithLock(dto.transactionId())
            .ifPresent(transaction -> {
                log.debug("Locking transaction is successfully done in database: {}", transaction.getId());
                switch (dto.status()) {
                    case FAILED -> failWithdrawalTransaction(transaction);
                    case COMPLETED -> completeWithdrawalTransaction(transaction);
                }
            });
    }

    private void failWithdrawalTransaction(Transaction transaction) {
        walletService.credit(transaction.getWallet().getId(), transaction.getAmount());
        transaction.setStatus(TransactionStatus.FAILED);
        repository.save(transaction);
        log.error("Withdrawal transaction failed: {}", transaction.getId());
    }

    private void completeWithdrawalTransaction(Transaction transaction) {
        transaction.setStatus(TransactionStatus.COMPLETED);
        repository.save(transaction);
        log.info("Withdrawal transaction successfully executed: {}", transaction.getId());
    }

    public TransactionDto getStatus(String transactionId) {
        var transaction = transactionRepository.findById(UUID.fromString(transactionId))
            .orElseThrow(() -> new TransactionServiceException("Transaction is not found by id=%s", transactionId));
        return transactionMapper.toTransactionDto(transaction);
    }

    public TransactionPageDto findAll(TransactionPageRequestDto dto) {
        var transactions = transactionRepository.findAllByFilters(
            toArray(dto.userIds(), UUID::fromString, UUID[]::new),
            toArray(dto.walletIds(), UUID::fromString, UUID[]::new),
            toArray(dto.types(), Enum::name, String[]::new),
            toArray(dto.statuses(), Enum::name, String[]::new),
            dto.dateFrom(),
            dto.dateTo(),
            dto.limit(),
            dto.offset()
        );
        var items = transactionMapper.from(transactions);

        return new TransactionPageDto()
            .count(transactions.size())
            .items(items);
    }
}
