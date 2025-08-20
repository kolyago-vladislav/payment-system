package com.example.payment.provider.business.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.payment.provider.business.mapper.TransactionMapper;
import com.example.payment.provider.business.repository.TransactionRepository;
import com.example.payment.provider.core.exception.PaymentProviderException;
import com.example.payment.provider.dto.StatusUpdateDto;
import com.example.payment.provider.dto.TransactionDto;
import com.example.payment.provider.dto.TransactionWriteDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper mapper;
    private final TransactionRepository repository;

    @Transactional
    public TransactionDto createTransaction(TransactionWriteDto transactionWriteDto) {
        var transaction = mapper.to(transactionWriteDto);
        repository.save(transaction);

        log.info("Transaction was successfully created id={}", transaction.getId());

        return mapper.from(transaction);
    }

    public TransactionDto findByTransactionId(Long id) {
        var transaction = repository.findById(id)
            .orElseThrow(() -> new PaymentProviderException("Transaction not found by id: %s", id));

        log.info("Transaction was successfully found id={}", transaction.getId());

        return mapper.from(transaction);
    }

    @Transactional
    public void updateStatus(StatusUpdateDto statusUpdateDto) {
        var transaction = repository.findById(statusUpdateDto.getId())
            .orElseThrow(() -> new PaymentProviderException("Transaction not found by id: %s", statusUpdateDto.getId()));

        transaction.setStatus(statusUpdateDto.getStatus());

        repository.save(transaction);
    }
}
