package com.example.payment.provider.entrypoint.controller.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.payment.provider.core.exception.PaymentProviderException;
import com.example.payment.provider.dto.ErrorResponse;

@RestControllerAdvice
public class ControllerErrorAdvice {

    @ExceptionHandler(PaymentProviderException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(PaymentProviderException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse().error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse().error(ex.getMessage()));
    }
}
