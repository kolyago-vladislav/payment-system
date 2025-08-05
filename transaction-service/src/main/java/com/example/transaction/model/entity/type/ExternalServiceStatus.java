package com.example.transaction.model.entity.type;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ExternalServiceStatus {
    COMPLETED,
    FAILED;

    @JsonCreator
    public static ExternalServiceStatus fromValue(String value) {
        return EnumUtil.from(ExternalServiceStatus.class, value, () -> new TransactionServiceException("Unknown ExternalServiceStatus: %s", value));
    }
}
