package com.example.config.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.exception.IndividualException;
import com.example.individual.dto.TransactionTypeDto;
import com.example.util.EnumUtil;

@Component
public class TransactionTypeConverter implements Converter<String, TransactionTypeDto> {

    @Override
    public TransactionTypeDto convert(@NonNull String value) {
        return EnumUtil.from(
            TransactionTypeDto.class,
            value,
            () -> new IndividualException("Unknown %s: %s", TransactionTypeDto.class.getSimpleName(), value)
        );
    }
}