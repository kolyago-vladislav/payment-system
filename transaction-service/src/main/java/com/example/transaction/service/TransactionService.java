package com.example.transaction.service;

import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionType;
import com.example.transaction.service.init.base.InitRequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final Map<TransactionType, InitRequestHandler> initRequestHandlers;

    public TransactionInitResponse init(
        TransactionType type,
        InitRequest initRequest
    ) {
        return initRequestHandlers.get(type).handle(initRequest);
    }

}
