package com.example.transaction.service.transaction;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionInitResponse;
import com.example.transaction.entity.type.TransactionType;
import com.example.transaction.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.service.transaction.init.base.InitRequestHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final Map<TransactionType, InitRequestHandler> initRequestHandlers;
    private final Map<TransactionType, ConfirmRequestHandler> confirmRequestHandlers;


    public TransactionInitResponse init(
        String type,
        InitRequest initRequest
    ) {
        return initRequestHandlers.get(TransactionType.from(type)).handle(initRequest);
    }

    @Transactional
    public TransactionConfirmResponse confirm(
        String type,
        ConfirmRequest confirmRequest
    ) {
        return confirmRequestHandlers.get(TransactionType.from(type)).handle(confirmRequest);
    }

}
