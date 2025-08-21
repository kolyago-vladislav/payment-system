package com.example.payment.provider.core.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.example.payment.provider.core.exception.PaymentProviderException;

public class AuthHeaderDecoder {

    private final static String BASIC_PREFIX = "Basic ";
    private final static String SPLIT_SYMBOL = ":";

    public static String[] decode(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
            throw new PaymentProviderException("Missing or invalid Authorization header");
        }

        var token = authHeader.substring(BASIC_PREFIX.length());

        var credentials = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
        var parts = credentials.split(SPLIT_SYMBOL);

        if (parts.length != 2) {
            throw new PaymentProviderException("Invalid token format");
        }

        return parts;
    }
}
