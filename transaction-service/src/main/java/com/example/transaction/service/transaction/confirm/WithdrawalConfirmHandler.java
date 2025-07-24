package com.example.transaction.service.transaction.confirm;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionType;
import com.example.transaction.dto.WithdrawalConfirmRequest;
import com.example.transaction.dto.WithdrawalRequestDto;
import com.example.transaction.entity.OutboxEvent;
import com.example.transaction.entity.Transaction;
import com.example.transaction.external.mapper.ExternalMapper;
import com.example.transaction.mapper.TransactionMapper;
import com.example.transaction.repository.OutboxRepository;
import com.example.transaction.repository.TransactionRepository;
import com.example.transaction.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.util.DateTimeUtil;
import com.example.transaction.util.JsonWrapper;

import static com.example.transaction.dto.TransactionType.WITHDRAWAL;

@Component
@RequiredArgsConstructor
public class WithdrawalConfirmHandler implements ConfirmRequestHandler {

    private final TransactionMapper transactionMapper;
    private final OutboxRepository repository;
    private final JsonWrapper jsonWrapper;
    private final DateTimeUtil dateTimeUtil;
    private final TransactionRepository transactionRepository;
    private final ExternalMapper externalMapper;

    @Override
    public TransactionConfirmResponse handle(ConfirmRequest confirmRequest) {
        var request = (WithdrawalConfirmRequest) confirmRequest;
        var transaction = transactionRepository.save(transactionMapper.to(request, WITHDRAWAL));

        saveOutboxEvent(transaction);

        return transactionMapper.from(transaction);
    }

    private void saveOutboxEvent(Transaction transaction) {
        var payload = externalMapper.toWithdrawalRequestDto(transaction);
        var outboxEvent = buildOutboxEvent(payload);

        repository.save(outboxEvent);
    }

    private OutboxEvent buildOutboxEvent(WithdrawalRequestDto payload) {
        return new OutboxEvent()
            .setTransactionId(payload.transactionId())
            .setType(com.example.transaction.entity.type.TransactionType.WITHDRAWAL)
            .setPayload(jsonWrapper.write(payload))
            .setCreated(dateTimeUtil.now());
    }

    @Override
    public TransactionType getType() {
        return WITHDRAWAL;
    }
}