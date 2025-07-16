package com.example.exception;

import java.util.UUID;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class PersonException extends RuntimeException {

    private HttpStatus httpStatus;

    private PersonException() {
    }

    private PersonException(
        HttpStatus httpStatus,
        String message,
        Object... args
    ) {
        super(String.format(message, args));
        this.httpStatus = httpStatus;
    }

    public static PersonException userExists(String username) {
        return new PersonException(HttpStatus.CONFLICT, "Username already exists with email=[%s]", username);
    }

    public static PersonException notFound(String parameter) {
        return new PersonException(HttpStatus.NOT_FOUND, "Individual is not found by [%s]", parameter);
    }

    public static PersonException notFound(UUID id) {
        return new PersonException(HttpStatus.NOT_FOUND, "Individual is not found by id [%s]", id.toString());
    }

}
