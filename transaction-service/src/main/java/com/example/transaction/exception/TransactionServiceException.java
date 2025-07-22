package com.example.transaction.exception;

import static java.lang.String.format;

public class TransactionServiceException extends RuntimeException {

    public TransactionServiceException(String message) {
        super(message);
    }

    public TransactionServiceException(String message, Object ... args) {
        super(format(message, args));
    }

}
