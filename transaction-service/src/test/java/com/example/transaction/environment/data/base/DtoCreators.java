package com.example.transaction.environment.data.base;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.environment.data.OutboxEventDtoCreator;
import com.example.transaction.environment.data.TransactionDtoCreator;
import com.example.transaction.environment.data.WalletDtoCreator;

@Component
@RequiredArgsConstructor
public class DtoCreators {

    public final WalletDtoCreator wallet;
    public final TransactionDtoCreator transaction;
    public final OutboxEventDtoCreator outboxEvent;
}
