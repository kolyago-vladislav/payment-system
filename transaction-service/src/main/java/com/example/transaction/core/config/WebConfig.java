package com.example.transaction.core.config;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.transaction.core.converter.TransactionConfirmRequestHttpMessageConverter;
import com.example.transaction.core.converter.TransactionInitRequestHttpMessageConverter;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final TransactionInitRequestHttpMessageConverter initConverter;
    private final TransactionConfirmRequestHttpMessageConverter confirmConverter;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addFirst(initConverter);
        converters.addFirst(confirmConverter);
    }
}
