package com.example.service;

import reactor.core.publisher.Mono;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.client.KeycloakClient;
import com.example.config.property.KeycloakProperties;
import com.example.dto.TokenResponse;
import com.example.dto.UserInfoResponse;
import com.example.dto.UserLoginRequest;
import com.example.dto.UserRegistrationRequest;
import com.example.exception.IndividualException;
import com.example.mapper.KeycloakMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakProperties keycloakProperties;
    private final KeycloakMapper keycloakMapper;
    private final TokenService tokenService;
    private final KeycloakClient keycloakClient;

    public Mono<UserInfoResponse> getCurrentUserInfo() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .flatMap(UserService::getUserInfoResponseMono)
            .switchIfEmpty(Mono.error(new IndividualException("No authentication present")));
    }

    private static Mono<UserInfoResponse> getUserInfoResponseMono(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            var userInfoResponse = new UserInfoResponse();
            userInfoResponse.setId(jwt.getSubject());
            userInfoResponse.setEmail(jwt.getClaimAsString("email"));
            userInfoResponse.setRoles(jwt.getClaimAsStringList("roles"));

            if (jwt.getIssuedAt() != null) {
                userInfoResponse.setCreatedAt(jwt.getIssuedAt().atOffset(ZoneOffset.UTC));
            }
            log.info("User[email={}] was successfully get info", jwt.getClaimAsString("email"));

            return Mono.just(userInfoResponse);
        }

        log.error("Can not get current user info: Invalid principal");
        return Mono.error(new IndividualException("Can not get current user info: Invalid principal"));
    }

    public Mono<TokenResponse> register(UserRegistrationRequest request) {
        var userRepresentation = keycloakMapper.toUserRepresentation(request);

        var adminLoginRequest = new UserLoginRequest(
            keycloakProperties.adminEmail(),
            keycloakProperties.adminPassword()
        );

        return tokenService.login(adminLoginRequest)
            .flatMap(adminTokenResponse ->
                keycloakClient.registerUser(request, adminTokenResponse, userRepresentation)
                    .flatMap(userId ->
                        keycloakClient.resetUserPassword(userId, keycloakMapper.toCredentialRepresentation(request), adminTokenResponse.getAccessToken())
                            .then(Mono.defer(() -> tokenService.login(new UserLoginRequest(request.getEmail(), request.getPassword()))))));
    }
}
