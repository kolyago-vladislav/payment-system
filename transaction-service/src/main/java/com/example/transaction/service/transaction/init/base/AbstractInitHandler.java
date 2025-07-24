package com.example.transaction.service.transaction.init.base;

import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionInitResponse;

public abstract class AbstractInitHandler<T extends InitRequest> implements InitRequestHandler {

    protected abstract Class<T> getRequestType();
    protected abstract TransactionInitResponse doAction(T request);

    @Override
    public TransactionInitResponse handle(InitRequest request) {
        return doAction(getRequestType().cast(request));
    }
}
