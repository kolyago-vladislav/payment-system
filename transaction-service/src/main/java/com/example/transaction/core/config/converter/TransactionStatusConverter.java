package com.example.transaction.core.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.EnumUtil;
import com.example.transaction.dto.TransactionStatusDto;

@Component
public class TransactionStatusConverter implements Converter<String, TransactionStatusDto> {

    @Override
    public TransactionStatusDto convert(@NonNull String value) {
        return EnumUtil.from(TransactionStatusDto.class, value, () -> new TransactionServiceException("Unknown TransactionStatusDto: %s", value));
    }
}