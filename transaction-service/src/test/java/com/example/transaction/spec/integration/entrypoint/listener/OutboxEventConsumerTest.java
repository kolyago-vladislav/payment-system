package com.example.transaction.spec.integration.entrypoint.listener;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.transaction.entrypoint.listener.OutboxEventConsumer;
import com.example.transaction.model.entity.OutboxEvent;
import com.example.transaction.spec.integration.NecessaryDependencyConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static com.example.transaction.core.util.TransactionCons.TRACE_ID_KEY;

public class OutboxEventConsumerTest extends NecessaryDependencyConfig {

    @Autowired
    private OutboxEventConsumer outboxEventConsumer;
    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;

    private String walletId;

    @BeforeEach
    void setUp() {
        reset(kafkaTemplate);

        var walletWriteDto = dtoCreator.wallet.createWalletWriteDto();

        var response = walletApiTestService.register(walletWriteDto);

        walletId = response;

        assertNotNull(response);
    }

    @Test
    public void shouldSendToExternalSystemTopicDepositEvent() {
        // given
        var outboxEvent = prepareDepositOutboxEvent();
        var bytes = jsonWrapper.write(outboxEvent).getBytes();

        var future = mock(CompletableFuture.class);
        when(kafkaTemplate.send(any(Message.class))).thenReturn(future);

        // when
        outboxEventConsumer.processMessage(bytes);

        // then
        var captor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate, times(1)).send(captor.capture());
        var sentMessage = captor.getValue();

        assertEquals(dtoCreator.outboxEvent.getDepositRequestOutboxEvent(walletId, outboxEvent.getTransactionId().toString()), sentMessage.getPayload());
        assertEquals(outboxEvent.getTraceId(), sentMessage.getHeaders().get(TRACE_ID_KEY));
        assertEquals(outboxEvent.getTransactionId().toString(), sentMessage.getHeaders().get(KafkaHeaders.KEY));
        verifyNoMoreInteractions(kafkaTemplate);
    }

    @Test
    public void shouldSendToExternalSystemTopicWithdrawalEvent() {
        // given
        var outboxEvent = prepareWithdrawalOutboxEvent();
        var bytes = jsonWrapper.write(outboxEvent).getBytes();

        var future = mock(CompletableFuture.class);
        when(kafkaTemplate.send(any(Message.class))).thenReturn(future);

        // when
        outboxEventConsumer.processMessage(bytes);

        // then
        var captor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate, times(1)).send(captor.capture());
        var sentMessage = captor.getValue();

        assertEquals(dtoCreator.outboxEvent.getWithdrawalRequestOutboxEvent(walletId, outboxEvent.getTransactionId().toString()), sentMessage.getPayload());
        assertEquals(outboxEvent.getTraceId(), sentMessage.getHeaders().get(TRACE_ID_KEY));
        assertEquals(outboxEvent.getTransactionId().toString(), sentMessage.getHeaders().get(KafkaHeaders.KEY));
        verifyNoMoreInteractions(kafkaTemplate);
    }

    public OutboxEvent prepareDepositOutboxEvent() {
        var request = dtoCreator.transaction.createDepositConfirmTransactionDto(walletId);
        var transactionConfirmResponse = transactionApiTestService.confirmDeposit(request);
        var outboxEvent = outboxEventTestService.getOutboxEventByTransactionId(transactionConfirmResponse.getTransactionId());
        return dtoCreator.outboxEvent.createDepositOutboxEvent(walletId, outboxEvent.getTransactionId().toString(), outboxEvent.getTraceId());
    }

    public OutboxEvent prepareWithdrawalOutboxEvent() {
        var request = dtoCreator.transaction.createWithdrawalConfirmTransactionDto(walletId);
        var transactionConfirmResponse = transactionApiTestService.confirmWithdrawal(request);
        var outboxEvent = outboxEventTestService.getOutboxEventByTransactionId(transactionConfirmResponse.getTransactionId());
        return dtoCreator.outboxEvent.createWithdrawalOutboxEvent(walletId, outboxEvent.getTransactionId().toString(), outboxEvent.getTraceId());
    }
}
