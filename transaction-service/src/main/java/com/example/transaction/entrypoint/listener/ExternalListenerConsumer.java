package com.example.transaction.entrypoint.listener;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.transaction.business.service.KafkaService;
import com.example.transaction.core.util.DateTimeUtil;
import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.model.dto.DepositCompletedDto;
import com.example.transaction.model.dto.DepositRequestDto;
import com.example.transaction.model.dto.WithdrawalCompletedDto;
import com.example.transaction.model.dto.WithdrawalRequestDto;
import com.example.transaction.model.entity.type.ExternalServiceStatus;

import static com.example.transaction.business.service.outbox.event.DepositEventHandler.TOPIC_EXTERNAL_DEPOSIT;
import static com.example.transaction.business.service.outbox.event.WithdrawalEventHandler.TOPIC_EXTERNAL_WITHDRAWAL;

@Service
@RequiredArgsConstructor
public class ExternalListenerConsumer {

    public static final String TOPIC_TRANSACTION_DEPOSIT = "transaction.deposit.completed";
    public static final String TOPIC_TRANSACTION_WITHDRAWAL = "transaction.withdrawal.completed";

    private final KafkaService kafkaService;
    private final DateTimeUtil dateTimeUtil;
    private final JsonWrapper jsonWrapper;

    @KafkaListener(topics = TOPIC_EXTERNAL_DEPOSIT, groupId = "transaction-service", containerFactory = "kafkaListenerContainerFactory")
    public void processDepositEvent(ConsumerRecord<String, byte[]> record) {
        var depositRequestDto = jsonWrapper.read(record.value(), DepositRequestDto.class);
        var depositCompletedDto = new DepositCompletedDto(
            depositRequestDto.transactionId(), ExternalServiceStatus.COMPLETED, depositRequestDto.amount(), dateTimeUtil.now());

        kafkaService.send(TOPIC_TRANSACTION_DEPOSIT, depositRequestDto.transactionId().toString(), depositCompletedDto);
    }

    @KafkaListener(topics = TOPIC_EXTERNAL_WITHDRAWAL, groupId = "transaction-service", containerFactory = "kafkaListenerContainerFactory")
    public void processWithdrawalEvent(ConsumerRecord<String, byte[]> record) {
        var withdrawalRequestDto = jsonWrapper.read(record.value(), WithdrawalRequestDto.class);
        var withdrawalCompletedDto = new WithdrawalCompletedDto(
            withdrawalRequestDto.transactionId(), ExternalServiceStatus.COMPLETED, null, dateTimeUtil.now());

        kafkaService.send(TOPIC_TRANSACTION_WITHDRAWAL, withdrawalRequestDto.transactionId().toString(), withdrawalCompletedDto);
    }
}
