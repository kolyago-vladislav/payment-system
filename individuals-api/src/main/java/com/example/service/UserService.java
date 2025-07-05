package com.example.service;

import reactor.core.publisher.Mono;
import java.time.ZoneOffset;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.config.property.KeycloakProperties;
import com.example.dto.KeycloakUserRegistrationDto;
import com.example.dto.TokenResponse;
import com.example.dto.UserInfoResponse;
import com.example.dto.UserLoginRequest;
import com.example.dto.UserRegistrationRequest;
import com.example.exception.IndividualException;
import com.example.mapper.CredentialRepresentationMapper;
import com.example.mapper.UserRepresentationMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REGEX_GET_SUBSTRING_AFTER_LAST_SLASH = ".*/([^/]+)$";

    private final KeycloakProperties keycloakProperties;
    private final WebClient webClient;
    private final UserRepresentationMapper userRepresentationMapper;
    private final CredentialRepresentationMapper credentialRepresentationMapper;
    private final TokenService tokenService;
    private String userRegistrationUrl;
    private String userPasswordResetUrl;
    private String userByIdUrl;

    @PostConstruct
    public void init() {
        userRegistrationUrl = keycloakProperties.serverUrl() + "/admin/realms/" + keycloakProperties.realm() + "/users";
        userByIdUrl = userRegistrationUrl + "/{id}";
        userPasswordResetUrl = userByIdUrl + "/reset-password";
    }

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
        var userRepresentation = userRepresentationMapper.to(request);

        var adminLoginRequest = new UserLoginRequest(
            keycloakProperties.adminEmail(),
            keycloakProperties.adminPassword()
        );

        return tokenService.login(adminLoginRequest)
            .flatMap(adminTokenResponse -> registerUser(request, adminTokenResponse, userRepresentation));
    }

    private Mono<TokenResponse> registerUser(
        UserRegistrationRequest request,
        TokenResponse adminTokenResponse,
        KeycloakUserRegistrationDto userRepresentation
    ) {
        return webClient.post()
            .uri(userRegistrationUrl)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + adminTokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userRepresentation)
            .exchangeToMono(response -> resetPassword(request, adminTokenResponse, response));
    }

    private Mono<TokenResponse> resetPassword(
        UserRegistrationRequest request,
        TokenResponse adminTokenResponse,
        ClientResponse response
    ) {
        if (response.statusCode().equals(HttpStatus.CREATED)) {
            var urlWithRegisteredUserId = response.headers().asHttpHeaders().getLocation();
            if (urlWithRegisteredUserId == null) {
                return Mono.error(new IndividualException("Location header missing"));
            }
            var userId = extractIdFromPath(urlWithRegisteredUserId.getPath());

            return setUserPasswordReactive(userId, request, adminTokenResponse.getAccessToken())
                .then(Mono.defer(() -> tokenService.login(new UserLoginRequest(request.getEmail(), request.getPassword()))));
        } else {
            return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.error("User creation failed: {}", body);
                    return Mono.error(new IndividualException("User creation failed"));
                });
        }
    }

    private Mono<Void> setUserPasswordReactive(String userId, UserRegistrationRequest request, String adminAccessToken) {
        var credentialRepresentation = credentialRepresentationMapper.to(request);
        return webClient.put()
            .uri(userPasswordResetUrl, userId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(credentialRepresentation)
            .retrieve()
            .toBodilessEntity()
            .onErrorResume(e -> executeOnError(userId, adminAccessToken, e))
            .then();
    }

    private Mono<ResponseEntity<Void>> executeOnError(
        String userId,
        String adminAccessToken,
        Throwable e
    ) {
        return webClient.delete()
            .uri(userByIdUrl, userId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
            .retrieve()
            .toBodilessEntity()
            .doOnSuccess(resp -> log.info("User with id={} deleted due to password set failure", userId))
            .doOnError(delErr -> log.error("Failed to delete user with id={} after password set failure", userId, delErr))
            .then(Mono.error(e));
    }

    private String extractIdFromPath(String path) {
        return path.replaceAll(REGEX_GET_SUBSTRING_AFTER_LAST_SLASH, "$1");
    }
}
