package com.example.enivronment.config.testcontainer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.transaction.dto.WalletDto;
import com.example.transaction.dto.WalletPageDto;
import com.example.transaction.dto.WalletWriteDto;
import com.example.transaction.dto.WalletWriteResponseDto;

@Service
public class WalletApiTestService {

    private final RestTemplate restTemplate;
    private final String url;
    private final KeycloakApiTestService keycloakApiTestService;

    public WalletApiTestService(
        RestTemplate restTemplate,
        @Value("${server.port}")
        int port,
        KeycloakApiTestService keycloakApiTestService
    ) {
        this.restTemplate = restTemplate;
        this.url = "http://localhost:" + port + "/individual/v1";
        this.keycloakApiTestService = keycloakApiTestService;
    }

    public WalletDto findById(String id) {
        return findBy(url + "/wallets/" + id);
    }

    public WalletPageDto findByUserId(String id) {
        var headers = setUpAdminToken();

        var entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url + "/wallets/user/" + id,
            HttpMethod.GET,
            entity,
            WalletPageDto.class
        ).getBody();
    }

    public String register(WalletWriteDto dto) {
        var headers = setUpAdminToken();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
            url + "/wallets",
            HttpMethod.POST,
            entity,
            WalletWriteResponseDto.class
        ).getBody().getId();
    }

    WalletDto findBy(String url) {
        var headers = setUpAdminToken();

        var entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            WalletDto.class
        ).getBody();
    }

    private HttpHeaders setUpAdminToken() {
        var adminAccessToken = keycloakApiTestService.getAdminAccessToken();

        var headers = new HttpHeaders();
        headers.setBearerAuth(adminAccessToken);
        return headers;
    }

}
