package com.example.transaction.config;

import com.example.transaction.dto.TransactionType;
import com.example.transaction.service.init.base.InitRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    public Map<TransactionType, InitRequestHandler> initRequestHandlers(List<InitRequestHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(InitRequestHandler::getType, Function.identity()));
    }
}
