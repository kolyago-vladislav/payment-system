package com.example.transaction.model.entity.type;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.EnumUtil;
import com.example.transaction.dto.TransactionTypeDto;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType {

    DEPOSIT,
    WITHDRAWAL,
    TRANSFER;

    @JsonCreator
    public static TransactionType from(String value) {
        return EnumUtil.from(TransactionType.class, value, () -> new TransactionServiceException("Unknown TransactionType: %s", value));
    }

    public static TransactionType from(TransactionTypeDto value) {
        return from(value.getValue());
    }

}