package com.example.transaction.service;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.dto.TransactionType;
import com.example.transaction.service.confirm.base.ConfirmRequestHandler;
import com.example.transaction.service.init.base.InitRequestHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final Map<TransactionType, InitRequestHandler> initRequestHandlers;
    private final Map<TransactionType, ConfirmRequestHandler> confirmRequestHandlers;


    public TransactionInitResponse init(
        TransactionType type,
        InitRequest initRequest
    ) {
        return initRequestHandlers.get(type).handle(initRequest);
    }

    @Transactional
    public TransactionConfirmResponse confirm(
        TransactionType type,
        ConfirmRequest confirmRequest
    ) {
        return confirmRequestHandlers.get(type).handle(confirmRequest);
    }

}
