package com.example.transaction.entrypoint.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.transaction.model.entity.type.TransactionType;

@Component
public class TransactionTypeConverter implements Converter<String, TransactionType> {

    @Override
    public TransactionType convert(@NonNull String source) {
        return TransactionType.from(source);
    }
}