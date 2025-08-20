package com.example.payment.provider.core.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.example.payment.provider.core.exception.PaymentProviderException;
import com.example.payment.provider.model.dto.MerchantPrincipal;

public class SecurityUtils {

    public static String getMerchantName() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof MerchantPrincipal principal) {
            return principal.getName();
        }

        throw new PaymentProviderException("No merchant principal found");
    }
}
