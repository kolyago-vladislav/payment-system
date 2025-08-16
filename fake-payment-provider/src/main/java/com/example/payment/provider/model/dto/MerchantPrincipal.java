package com.example.payment.provider.model.dto;

import java.security.Principal;

public record MerchantPrincipal(String name, String secretKey) implements Principal {

    @Override
    public String getName() {
        return name;
    }
}