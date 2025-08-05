package com.example.enivronment.config.testcontainer.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.individual.dto.ConfirmRequest;
import com.example.individual.dto.TransactionConfirmResponse;
import com.example.individual.dto.TransactionDto;
import com.example.individual.dto.TransactionPageDto;

import static java.util.Optional.ofNullable;

@Service
public class TransactionApiTestService {
    private final RestTemplate restTemplate;
    private final String url;
    private final KeycloakApiTestService keycloakApiTestService;

    public TransactionApiTestService(
        RestTemplate restTemplate,
        @Value("${server.port}")
        int port,
        KeycloakApiTestService keycloakApiTestService
    ) {
        this.restTemplate = restTemplate;
        this.url = "http://localhost:" + port + "/individual/v1";
        this.keycloakApiTestService = keycloakApiTestService;
    }

    public TransactionConfirmResponse confirmDeposit(ConfirmRequest dto) {
        return confirm(dto, "/transactions/deposit/confirm");
    }

    public TransactionConfirmResponse confirmWithdrawal(ConfirmRequest dto) {
        return confirm(dto, "/transactions/withdrawal/confirm");
    }

    public TransactionConfirmResponse confirmTransfer(ConfirmRequest dto) {
        return confirm(dto, "/transactions/transfer/confirm");
    }

    private @Nullable TransactionConfirmResponse confirm(ConfirmRequest dto, String path) {
        var headers = setUpAdminToken();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
            url + path,
            HttpMethod.POST,
            entity,
            TransactionConfirmResponse.class
        ).getBody();
    }

    public TransactionPageDto findAll(List<String> walletIds) {
        var headers = setUpAdminToken();

        var entity = new HttpEntity<>(headers);
        var path = buildPath(url + "/transactions", walletIds);
        return restTemplate.exchange(
            path,
            HttpMethod.GET,
            entity,
            TransactionPageDto.class
        ).getBody();
    }

    private String buildPath(String path, List<String> walletIds) {
        var params = new ArrayList<String>();

        ofNullable(walletIds).ifPresent(ids -> ids.forEach(id -> params.add("walletId=" + encode(id))));

        return path + "?" + String.join("&", params);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public TransactionDto findByTransactionId(String id) {
        var headers = setUpAdminToken();

        var entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url + "/transactions/%s/status".formatted(id),
            HttpMethod.GET,
            entity,
            TransactionDto.class
        ).getBody();
    }

    private HttpHeaders setUpAdminToken() {
        var adminAccessToken = keycloakApiTestService.getAdminAccessToken();

        var headers = new HttpHeaders();
        headers.setBearerAuth(adminAccessToken);
        return headers;
    }

}
