package com.example.transaction.business.service.transaction.init.base;

import java.util.List;

import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.business.validation.base.TransactionValidator;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.model.dto.ValidationContext;

@Setter(onMethod_ = {@Autowired})
public abstract class AbstractInitHandler<T extends InitRequest> implements InitRequestHandler {

    protected List<TransactionValidator> validators;

    protected abstract ValidationContext getValidationContext(T dto);
    protected abstract Class<T> getRequestType();
    protected abstract TransactionInitResponse doAction(T request);

    @Override
    public TransactionInitResponse handle(InitRequest request) {
        var dto = getRequestType().cast(request);

        validators.stream()
            .filter(validator -> validator.getSupportTransactionTypes().contains(getType()))
            .forEach(validator -> validator.validate(getValidationContext(dto)));

        return doAction(dto);
    }
}
