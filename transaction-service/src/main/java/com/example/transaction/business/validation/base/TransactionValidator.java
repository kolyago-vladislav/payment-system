package com.example.transaction.business.validation.base;

import java.util.List;

import com.example.transaction.model.dto.ValidationContext;
import com.example.transaction.model.entity.type.TransactionType;

public interface TransactionValidator {

    void validate(ValidationContext context);
    List<TransactionType> getSupportTransactionTypes();
}
