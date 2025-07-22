package com.example.transaction.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.transaction.dto.TransactionType;
import com.example.transaction.util.TransactionTypeUtil;

@Component
public class TransactionTypeConverter implements Converter<String, TransactionType> {

    @Override
    public TransactionType convert(@NonNull String source) {
        return TransactionTypeUtil.from(source);
    }
}