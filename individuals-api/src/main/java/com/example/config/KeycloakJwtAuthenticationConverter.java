package com.example.config;

import reactor.core.publisher.Flux;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        var roles = jwt.getClaimAsStringList("roles");
        if (roles == null) {
            return Flux.empty();
        }

        return Flux.fromIterable(roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList()));
    }
}