package com.example.transaction.model.entity.type;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.EnumUtil;
import com.example.transaction.dto.TransactionStatusDto;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionStatus {

    COMPLETED,
    CONFIRMED,
    FAILED,
    PENDING;

    @JsonCreator
    public static TransactionStatus from(String value) {
        return EnumUtil.from(TransactionStatus.class, value, () -> new TransactionServiceException("Unknown TransactionStatus: %s", value));
    }

    public static TransactionStatus from(TransactionStatusDto value) {
        return from(value.getValue());
    }

}