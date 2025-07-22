package com.example.transaction.util;

import lombok.experimental.UtilityClass;

import com.example.transaction.dto.TransactionType;
import com.example.transaction.exception.TransactionServiceException;

@UtilityClass
public class TransactionTypeUtil {

    public static TransactionType from(String value) {
        for (var type : TransactionType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new TransactionServiceException("Unknown TransactionType: %s", value);
    }
}
