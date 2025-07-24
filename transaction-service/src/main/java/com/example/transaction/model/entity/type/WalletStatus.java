package com.example.transaction.model.entity.type;

import com.example.transaction.core.exception.TransactionServiceException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum WalletStatus {

    ACTIVE,

    ARCHIVED,

    BLOCKED;

    @JsonCreator
    public static WalletStatus fromValue(String value) {
        for (var type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TransactionServiceException("Unknown TransactionType: %s", value);
    }

}