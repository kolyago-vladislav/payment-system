package com.example.transaction.entrypoint.controller.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.transaction.core.exception.TransactionServiceException;
import com.example.transaction.dto.ErrorResponse;

@RestControllerAdvice
public class ControllerErrorAdvice {

    @ExceptionHandler(TransactionServiceException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(TransactionServiceException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse()
                .message(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse()
                .message(ex.getMessage()));
    }
}
