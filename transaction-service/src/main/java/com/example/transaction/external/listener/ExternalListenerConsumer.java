package com.example.transaction.external.listener;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.transaction.model.dto.DepositCompleted;
import com.example.transaction.model.dto.DepositRequestDto;
import com.example.transaction.model.dto.WithdrawalRequestDto;
import com.example.transaction.model.dto.WithdrawalCompletedDto;
import com.example.transaction.business.service.KafkaService;
import com.example.transaction.core.util.DateTimeUtil;
import com.example.transaction.core.util.JsonWrapper;

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
    public void processDepositEvent(byte[] messageBytes) {
        var depositRequestDto = jsonWrapper.read(messageBytes, DepositRequestDto.class);
        var depositCompletedDto = new DepositCompleted(depositRequestDto.transactionId(), "success", depositRequestDto.amount(), dateTimeUtil.now());

        kafkaService.send(TOPIC_TRANSACTION_DEPOSIT, depositRequestDto.transactionId().toString(), depositCompletedDto);
    }

    @KafkaListener(topics = TOPIC_EXTERNAL_WITHDRAWAL, groupId = "transaction-service", containerFactory = "kafkaListenerContainerFactory")
    public void processWithdrawalEvent(byte[] messageBytes) {
        var withdrawalRequestDto = jsonWrapper.read(messageBytes, WithdrawalRequestDto.class);
        var withdrawalCompletedDto = new WithdrawalCompletedDto(withdrawalRequestDto.transactionId(), "success", null, dateTimeUtil.now());

        kafkaService.send(TOPIC_TRANSACTION_WITHDRAWAL, withdrawalRequestDto.transactionId().toString(), withdrawalCompletedDto);
    }
}
