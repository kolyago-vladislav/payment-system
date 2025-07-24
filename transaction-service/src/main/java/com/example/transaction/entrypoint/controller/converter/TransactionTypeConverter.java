package com.example.transaction.entrypoint.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.EnumUtil;
import com.example.transaction.dto.TransactionTypeDto;

@Component
public class TransactionTypeConverter implements Converter<String, TransactionTypeDto> {

    @Override
    public TransactionTypeDto convert(@NonNull String value) {
        return EnumUtil.from(TransactionTypeDto.class, value, () -> new TransactionServiceException("Unknown ExternalServiceStatus: %s", value));
    }
}