package com.example.config;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> {
                applyPublicRoutes(exchanges);
                applyProtectedRoutes(exchanges);
                exchanges.anyExchange().authenticated();
            })
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakAuthenticationConverter()))
            )
            .build();
    }

    private void applyPublicRoutes(ServerHttpSecurity.AuthorizeExchangeSpec exchanges) {
        exchanges
            .pathMatchers(
                "/actuator/prometheus",
                "/actuator/health",
                "/individual/v1/auth/registration",
                "/individual/v1/auth/login",
                "/individual/v1/auth/refresh-token"
            ).permitAll();
    }

    private void applyProtectedRoutes(ServerHttpSecurity.AuthorizeExchangeSpec exchanges) {
        exchanges
            .pathMatchers(HttpMethod.PUT, "/individual/v1/persons/*").authenticated()
            .pathMatchers(HttpMethod.DELETE, "/individual/v1/persons/*").authenticated()
            .pathMatchers(
                "/actuator/**",
                "/individual/v1/persons/**"
            ).hasRole("individual.admin");
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> keycloakAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtAuthenticationConverter());
        return converter;
    }

    private Converter<Jwt, Flux<GrantedAuthority>> jwtAuthenticationConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }
}