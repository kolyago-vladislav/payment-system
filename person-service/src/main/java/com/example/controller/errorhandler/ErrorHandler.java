package com.example.controller.errorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.PersonException;
import com.example.person.dto.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(PersonException.class)
    public ResponseEntity<ErrorResponse> handleIndividualException(PersonException ex) {
        return ResponseEntity
            .status(ex.getHttpStatus())
            .body(new ErrorResponse().error(ex.getMessage()));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse().error(ex.getMessage()));

    }
}
