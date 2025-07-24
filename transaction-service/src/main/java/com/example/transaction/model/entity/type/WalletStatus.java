package com.example.transaction.model.entity.type;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.core.util.EnumUtil;
import com.example.transaction.dto.WalletStatusDto;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum WalletStatus {

    ACTIVE,
    ARCHIVED,
    BLOCKED;

    @JsonCreator
    public static WalletStatus from(String value) {
        return EnumUtil.from(WalletStatus.class, value, () -> new TransactionServiceException("Unknown WalletStatus: %s", value));
    }

    public static WalletStatus from(WalletStatusDto value) {
        return from(value.getValue());
    }

}