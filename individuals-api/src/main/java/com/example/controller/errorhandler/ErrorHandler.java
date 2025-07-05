package com.example.controller.errorhandler;

import reactor.core.publisher.Mono;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exception.IndividualException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IndividualException.class)
    public Mono<ResponseEntity<Map<String, String>>> handleIndividualException(IndividualException ex) {
        return Mono.just(
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", ex.getMessage()))
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> handleGenericException(Exception ex) {
        return Mono.just(
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "An unexpected error occurred"))
        );
    }
}
