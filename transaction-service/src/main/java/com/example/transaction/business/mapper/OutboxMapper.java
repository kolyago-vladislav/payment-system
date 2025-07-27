package com.example.transaction.business.mapper;

import java.util.UUID;

import lombok.Setter;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.slf4j.MDC;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.transaction.core.util.DateTimeUtil;
import com.example.transaction.model.entity.OutboxEvent;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
@Setter(onMethod_ = {@Autowired})
public abstract class OutboxMapper {

    protected DateTimeUtil dateTimeUtil;

    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "payload", source = "payload")
    @Mapping(target = "type", constant = "DEPOSIT")
    public abstract OutboxEvent toDepositEvent(
        UUID transactionId,
        String payload
    );

    @Mapping(target = "createdAt", expression = "java(dateTimeUtil.now())")
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "payload", source = "payload")
    @Mapping(target = "type", constant = "WITHDRAWAL")
    public abstract OutboxEvent toWithdrawalEvent(
        UUID transactionId,
        String payload
    );

    @AfterMapping
    protected void afterMapping(
        @MappingTarget
        OutboxEvent outboxEvent
    ) {
        outboxEvent.setTraceId(MDC.get("traceId"));
    }
}
