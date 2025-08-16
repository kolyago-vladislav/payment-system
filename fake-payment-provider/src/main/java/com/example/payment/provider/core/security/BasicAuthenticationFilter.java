package com.example.payment.provider.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.example.payment.provider.model.dto.MerchantPrincipal;

import static com.example.payment.provider.core.security.AuthHeaderDecoder.decode;

public class BasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String MERCHANT_AUTHORITY = "MERCHANT";

    public BasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super("/**");
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult
    ) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {
        var parts = decode(request.getHeader(HttpHeaders.AUTHORIZATION));

        var merchantPrincipal = new MerchantPrincipal(parts[0], parts[1]);

        var token = new UsernamePasswordAuthenticationToken(merchantPrincipal, null, null);

        return getAuthenticationManager().authenticate(token);
    }

}