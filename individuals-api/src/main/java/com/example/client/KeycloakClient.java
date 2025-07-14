package com.example.client;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.config.property.KeycloakProperties;
import com.example.dto.KeycloakAccessTokenResponse;
import com.example.dto.KeycloakCredentialRepresentation;
import com.example.dto.KeycloakUserRepresentation;
import com.example.exception.IndividualException;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.TokenRefreshRequest;
import com.example.individual.dto.TokenResponse;
import com.example.individual.dto.UserLoginRequest;
import com.example.util.UserIdExtractor;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.PASSWORD;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.USERNAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakClient {

    public static final String BEARER_PREFIX = "Bearer ";

    private final WebClient webClient;
    private final KeycloakProperties keycloakProperties;
    private String userRegistrationUrl;
    private String userPasswordResetUrl;
    private String userByIdUrl;

    @PostConstruct
    public void init() {
        userRegistrationUrl = keycloakProperties.serverUrl() + "/admin/realms/" + keycloakProperties.realm() + "/users";
        userByIdUrl = userRegistrationUrl + "/{id}";
        userPasswordResetUrl = userByIdUrl + "/reset-password";
    }

    public Mono<KeycloakAccessTokenResponse> login(UserLoginRequest userLoginRequest) {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add(GRANT_TYPE, PASSWORD);
        formData.add(USERNAME, userLoginRequest.getEmail());
        formData.add(PASSWORD, userLoginRequest.getPassword());
        formData.add(CLIENT_ID, keycloakProperties.clientId());
        formData.add(CLIENT_SECRET, keycloakProperties.clientSecret());

        return webClient.post()
            .uri(keycloakProperties.tokenUrl()) // обычно "/realms/{realm}/protocol/openid-connect/token"
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(formData)
            .retrieve()
            .bodyToMono(KeycloakAccessTokenResponse.class);
    }

    public Mono<KeycloakAccessTokenResponse> refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        var formData = new LinkedMultiValueMap<String, String>();
        formData.add(GRANT_TYPE, REFRESH_TOKEN);
        formData.add(REFRESH_TOKEN, tokenRefreshRequest.getRefreshToken());
        formData.add(CLIENT_ID, keycloakProperties.clientId());
        formData.add(CLIENT_SECRET, keycloakProperties.clientSecret());

        return webClient.post()
            .uri(keycloakProperties.tokenUrl())
            .bodyValue(formData)
            .retrieve()
            .bodyToMono(KeycloakAccessTokenResponse.class);
    }

    @WithSpan(value = "keycloakClient.registerUser")
    public Mono<String> registerUser(
        IndividualWriteDto dto,
        TokenResponse adminTokenResponse,
        KeycloakUserRepresentation userRepresentation
    ) {
        return webClient.post()
            .uri(userRegistrationUrl)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + adminTokenResponse.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userRepresentation)
            .exchangeToMono(this::extractIdFromPath)
            .doOnNext(id -> log.info("User with id={} registered", id));
    }

    private Mono<String> extractIdFromPath(ClientResponse response) {
        if (response.statusCode().equals(HttpStatus.CREATED)) {
            var urlWithRegisteredUserId = response.headers().asHttpHeaders().getLocation();
            if (urlWithRegisteredUserId == null) {
                return Mono.error(new IndividualException("Location header missing"));
            }

            return Mono.just(UserIdExtractor.extractIdFromPath(urlWithRegisteredUserId.getPath()));
        } else {
            return response.bodyToMono(String.class)
                .flatMap(body -> {
                    log.error("User creation failed: {}", body);
                    return Mono.error(new IndividualException("User creation failed"));
                });
        }
    }

    @WithSpan("keycloakClient.resetUserPassword")
    public Mono<Void> resetUserPassword(
        String userId,
        KeycloakCredentialRepresentation dto,
        String adminAccessToken
    ) {
        return webClient.put()
            .uri(userPasswordResetUrl, userId)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + adminAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(dto)
            .retrieve()
            .toBodilessEntity()
            .doOnNext(s -> log.info("Reset password request started for userId={}", userId))
            .onErrorResume(e -> executeOnError(userId, adminAccessToken, e))
            .then();
    }

    @WithSpan("keycloakClient.resetUserPassword.executeOnError")
    private Mono<ResponseEntity<Void>> executeOnError(
        String userId,
        String adminAccessToken,
        Throwable e
    ) {
        return webClient.delete()
            .uri(userByIdUrl, userId)
            .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + adminAccessToken)
            .retrieve()
            .toBodilessEntity()
            .doOnSuccess(resp -> log.info("User with id={} deleted due to password set failure", userId))
            .doOnError(delErr -> log.error("Failed to delete user with id={} after password set failure", userId, delErr))
            .then(Mono.error(e));
    }
}
