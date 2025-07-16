package com.example.controller.errorhandler;

import reactor.core.publisher.Mono;

import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.IndividualException;
import com.example.exception.WebServiceException;
import com.example.individual.dto.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(WebServerException.class)
    public ResponseEntity<com.example.person.dto.ErrorResponse> handleIndividualException(WebServiceException ex) {
        return ResponseEntity
            .status(ex.getStatus())
            .body(new com.example.person.dto.ErrorResponse().error(ex.getMessage()));

    }

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
