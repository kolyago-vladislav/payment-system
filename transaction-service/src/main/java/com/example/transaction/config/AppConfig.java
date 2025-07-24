package com.example.transaction.config;

import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.transaction.dto.TransactionType;
import com.example.transaction.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.service.outbox.event.base.OutboxEventHandler;
import com.example.transaction.service.transaction.init.base.InitRequestHandler;

@Configuration
public class AppConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneOffset.UTC);
    }

    @Bean
    public Map<TransactionType, InitRequestHandler> initRequestHandlers(List<InitRequestHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(InitRequestHandler::getType, Function.identity()));
    }

    @Bean
    public Map<TransactionType, ConfirmRequestHandler> confirmRequestHandlers(List<ConfirmRequestHandler> handlers) {
        return handlers.stream()
            .collect(Collectors.toMap(ConfirmRequestHandler::getType, Function.identity()));
    }

    @Bean
    public Map<com.example.transaction.entity.type.TransactionType, OutboxEventHandler<?>> outboxEventHandlers(List<OutboxEventHandler<?>> handlers) {
        return handlers.stream()
            .collect(Collectors.toMap(OutboxEventHandler::getOutboxEventType, Function.identity()));
    }
}
