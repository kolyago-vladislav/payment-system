package com.example.payment.provider.core.exception;

import static java.lang.String.format;

public class PaymentProviderException extends RuntimeException {
    public PaymentProviderException(String message) {
        super(message);
    }

    public PaymentProviderException(String message, Object ... args) {
        super(format(message, args));
    }
}
