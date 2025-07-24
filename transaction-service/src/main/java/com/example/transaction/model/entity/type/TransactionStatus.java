package com.example.transaction.model.entity.type;

import com.example.transaction.core.exception.TransactionServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionStatus {

    CONFIRMED,

    FAILED,

    PENDING;

    @JsonCreator
    public static TransactionStatus fromValue(String value) {
        for (var type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TransactionServiceException("Unknown TransactionType: %s", value);
    }

}