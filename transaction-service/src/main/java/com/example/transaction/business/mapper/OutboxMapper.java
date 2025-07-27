package com.example.transaction.business.mapper;

import lombok.Setter;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.slf4j.MDC;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.core.util.DateTimeUtil;
import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.model.entity.Transaction;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import static com.example.transaction.core.util.TransactionCons.TRACE_ID_KEY;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class OutboxMapper {

    protected DateTimeUtil dateTimeUtil;

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "userId", source = "transaction.userId")
    @Mapping(target = "payload", source = "payload")
    @Mapping(target = "type", constant = "DEPOSIT")
    public abstract OutboxEvent toDepositEvent(
        Transaction transaction,
        String payload
    );

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "userId", source = "transaction.userId")
    @Mapping(target = "payload", source = "payload")
    @Mapping(target = "type", constant = "WITHDRAWAL")
    public abstract OutboxEvent toWithdrawalEvent(
        Transaction transaction,
        String payload
    );

    @AfterMapping
    protected void afterMapping(
        @MappingTarget
        OutboxEvent outboxEvent
    ) {
        outboxEvent.setTraceId(MDC.get(TRACE_ID_KEY));
    }
}
