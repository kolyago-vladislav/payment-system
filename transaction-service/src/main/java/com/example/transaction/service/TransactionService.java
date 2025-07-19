package com.example.transaction.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransactionInitResponse;

@Slf4j
@Service
public class TransactionService {

    public TransactionInitResponse init(
        String type,
        InitRequest initRequest
    ) {
        log.info("Transaction was successfully processed");

        return new TransactionInitResponse();
    }
}
