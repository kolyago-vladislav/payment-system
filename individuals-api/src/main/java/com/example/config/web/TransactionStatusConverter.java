package com.example.config.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.exception.IndividualException;
import com.example.individual.dto.TransactionStatusDto;
import com.example.util.EnumUtil;

@Component
public class TransactionStatusConverter implements Converter<String, TransactionStatusDto> {

    @Override
    public TransactionStatusDto convert(@NonNull String value) {
        return EnumUtil.from(
            TransactionStatusDto.class,
            value,
            () -> new IndividualException("Unknown %s: %s", TransactionStatusDto.class.getSimpleName(), value)
        );
    }
}