package com.example.payment.provider.core.security;

import java.util.Collections;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.payment.provider.business.service.MerchantService;
import com.example.payment.provider.core.exception.PaymentProviderException;
import com.example.payment.provider.model.dto.MerchantPrincipal;

@Component
@RequiredArgsConstructor
public class MerchantAuthenticationProvider implements AuthenticationProvider {

    private final MerchantService merchantService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var principal = (MerchantPrincipal) authentication.getPrincipal();

        var merchant = merchantService.findById(principal.getName())
            .orElseThrow(() -> new PaymentProviderException("Merchant not found by name: %s", principal.getName()));

        if (!merchant.getSecretKey().equals(principal.secretKey())) {
            throw new PaymentProviderException("Invalid secret key");
        }

        return new UsernamePasswordAuthenticationToken(principal, principal.secretKey(), Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
