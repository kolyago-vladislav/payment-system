package com.example.transaction.entrypoint.listener;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.transaction.business.service.transaction.TransactionService;
import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.model.dto.DepositCompletedDto;
import com.example.transaction.model.dto.WithdrawalCompletedDto;

@Service
@RequiredArgsConstructor
public class CompleteTransactionEventConsumer {

    private final TransactionService transactionService;
    private final JsonWrapper jsonWrapper;

    @KafkaListener(
        topics = "transaction.deposit.completed",
        groupId = "transaction-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void processDepositCompleteEvent(ConsumerRecord<String, byte[]> record) {
        transactionService.processDepositCompleteEvent(jsonWrapper.read(record.value(), DepositCompletedDto.class));
    }

    @KafkaListener(
        topics = "transaction.withdrawal.completed",
        groupId = "transaction-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void processWithdrawalCompleteEvent(ConsumerRecord<String, byte[]> record) {
        transactionService.processWithdrawalCompleteEvent(jsonWrapper.read(record.value(), WithdrawalCompletedDto.class));
    }
}
