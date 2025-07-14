package com.example.service;

import io.opentelemetry.instrumentation.annotations.WithSpan;
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
import com.example.dto.KeycloakCredentialRepresentation;
import com.example.dto.KeycloakUserRepresentation;
import com.example.exception.IndividualException;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.TokenResponse;
import com.example.individual.dto.UserInfoResponse;
import com.example.individual.dto.UserLoginRequest;
import com.example.mapper.KeycloakMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final PersonService personService;
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

    @WithSpan("userService.register")
    public Mono<TokenResponse> register(IndividualWriteDto request) {
        return personService.register(request)
            .flatMap(writeResponseDto ->
                tokenService.obtainAdminServiceToken()
                    .flatMap(adminTokenResponse ->
                        keycloakClient
                            .registerUser(request, adminTokenResponse, createRepresentation(request, writeResponseDto.getId()))
                            .flatMap(userId ->
                                keycloakClient
                                    .resetUserPassword(userId, toCredentialRepresentation(request), adminTokenResponse.getAccessToken())
                                    .then(Mono.defer(() -> tokenService.login(new UserLoginRequest(request.getEmail(), request.getPassword()))))))
                    .onErrorResume(throwable ->
                        personService
                            .hardDelete(writeResponseDto.getId())
                            .then(Mono.error(throwable)))
            );
    }

    private KeycloakCredentialRepresentation toCredentialRepresentation(IndividualWriteDto request) {
        return keycloakMapper.toCredentialRepresentation(request);
    }

    private KeycloakUserRepresentation createRepresentation(
        IndividualWriteDto request,
        String id
    ) {
        return keycloakMapper.toUserRepresentation(request, id);
    }
}
