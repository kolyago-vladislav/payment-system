package com.example.exception;

import static java.lang.String.format;

public class IndividualException extends RuntimeException {

    public IndividualException(String message) {
        super(message);
    }

    public IndividualException(String message, Object ... args) {
        super(format(message, args));
    }

}
