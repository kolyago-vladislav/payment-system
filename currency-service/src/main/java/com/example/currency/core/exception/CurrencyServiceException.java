
package com.example.currency.core.exception;

import static java.lang.String.format;

public class CurrencyServiceException extends RuntimeException {

    public CurrencyServiceException(String message) {
        super(message);
    }

    public CurrencyServiceException(String message, Object ... args) {
        super(format(message, args));
    }

}
