package com.example.transaction.model.entity.type;

import com.example.transaction.core.exception.TransactionServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType {

    DEPOSIT,

    WITHDRAWAL,

    TRANSFER;

    @JsonCreator
    public static TransactionType from(String value) {
        for (var type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TransactionServiceException("Unknown TransactionType: %s", value);
    }

}