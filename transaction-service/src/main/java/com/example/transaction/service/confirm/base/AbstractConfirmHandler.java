package com.example.transaction.service.confirm.base;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.entity.Transaction;
import com.example.transaction.mapper.TransactionMapper;
import com.example.transaction.repository.TransactionRepository;

@Setter(onMethod_ = {@Autowired})
public abstract class AbstractConfirmHandler<T extends ConfirmRequest> implements ConfirmRequestHandler {

    private TransactionRepository repository;
    private TransactionMapper transactionMapper;
    
    protected abstract Class<T> getRequestType();
    protected abstract Transaction map(T request);

    @Override
    public TransactionConfirmResponse handle(ConfirmRequest request) {
        var defineRequest = getRequestType().cast(request);
        var transaction = repository.save(map(defineRequest));

        return transactionMapper.from(transaction);
    }
}
