package com.example.exception;

import static java.lang.String.format;

public class PersonException extends RuntimeException {

    public PersonException(String message) {
        super(message);
    }

    public PersonException(String message, Object ... args) {
        super(format(message, args));
    }

}
