package com.example.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import com.example.config.web.TransactionConfirmRequestHttpMessageReader;
import com.example.config.web.TransactionInitRequestHttpMessageReader;
import com.example.config.web.TransactionStatusConverter;
import com.example.config.web.TransactionTypeConverter;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebFluxConfigurer {

    private final TransactionInitRequestHttpMessageReader initReader;
    private final TransactionConfirmRequestHttpMessageReader confirmReader;
    private final TransactionStatusConverter transactionStatusConverter;
    private final TransactionTypeConverter transactionTypeConverter;

    @Override
    public void configureHttpMessageCodecs(org.springframework.http.codec.ServerCodecConfigurer configurer) {
        configurer.customCodecs().register(initReader);
        configurer.customCodecs().register(confirmReader);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(transactionStatusConverter);
        registry.addConverter(transactionTypeConverter);
    }
}