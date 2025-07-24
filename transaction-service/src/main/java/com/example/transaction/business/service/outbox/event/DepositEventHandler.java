package com.example.transaction.business.service.outbox.event;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import com.example.transaction.model.dto.DepositRequestDto;
import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.model.entity.type.TransactionType;
import com.example.transaction.business.service.KafkaService;
import com.example.transaction.business.service.outbox.event.base.OutboxEventHandler;
import com.example.transaction.core.util.JsonWrapper;

@Component
@RequiredArgsConstructor
public class DepositEventHandler implements OutboxEventHandler<DepositRequestDto> {

    public static final String TOPIC_EXTERNAL_DEPOSIT = "external.deposit";
    private final KafkaService kafkaService;
    private final JsonWrapper jsonWrapper;

    @Override
    public TransactionType getOutboxEventType() {
        return TransactionType.DEPOSIT;
    }

    public void handle(OutboxEvent outboxEvent) {
        kafkaService.send(TOPIC_EXTERNAL_DEPOSIT, outboxEvent.getTransactionId().toString(), jsonWrapper.read(outboxEvent.getPayload(), getType()));
    }

    @Override
    public Class<DepositRequestDto> getType() {
        return DepositRequestDto.class;
    }
}
