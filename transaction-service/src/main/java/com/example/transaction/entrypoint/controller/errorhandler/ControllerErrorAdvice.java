package com.example.transaction.entrypoint.controller.errorhandler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.dto.ErrorResponse;

@RestControllerAdvice
public class ControllerErrorAdvice {

    @ExceptionHandler(TransactionServiceException.class)
    public ErrorResponse handleGenericException(TransactionServiceException ex) {
        return new ErrorResponse()
            .message(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception ex) {
        return new ErrorResponse()
            .message(ex.getMessage());
    }
}
