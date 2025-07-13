package com.example.controller.errorhandler;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.IndividualException;
import com.example.individual.dto.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IndividualException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIndividualException(IndividualException ex) {
        return Mono.just(
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse().error(ex.getMessage()))
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
        return Mono.just(
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse().error(ex.getMessage()))
        );
    }
}
