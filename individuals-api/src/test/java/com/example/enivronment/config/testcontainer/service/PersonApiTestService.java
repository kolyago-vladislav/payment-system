package com.example.enivronment.config.testcontainer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.individual.dto.IndividualDto;
import com.example.person.dto.IndividualWriteDto;
import com.example.person.dto.IndividualWriteResponseDto;

@Component
public class PersonApiTestService {

    private final RestTemplate restTemplate;
    private final String url;
    private final KeycloakApiTestService keycloakApiTestService;

    public PersonApiTestService(
        RestTemplate restTemplate,
        @Value("${server.port}")
        int port,
        KeycloakApiTestService keycloakApiTestService
    ) {
        this.restTemplate = restTemplate;
        this.url = "http://localhost:" + port;
        this.keycloakApiTestService = keycloakApiTestService;
    }

    public IndividualDto findById(String personId) {
        return findBy(url + "/v1/persons/" + personId);
    }

    public IndividualDto findByEmail(String email) {
        return findBy(url + "/v1/persons/email/" + email);
    }

    private IndividualDto findBy(String url) {
        var headers = setUpAdminToken();

        var entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            IndividualDto.class
        ).getBody();
    }

    private HttpHeaders setUpAdminToken() {
        var adminAccessToken = keycloakApiTestService.getAdminAccessToken();

        var headers = new HttpHeaders();
        headers.setBearerAuth(adminAccessToken);
        return headers;
    }

    public String update(
        String personId,
        IndividualWriteDto individualWriteDto
    ) {
        var headers = setUpAdminToken();

        var entity = new HttpEntity<>(individualWriteDto, headers);

        return restTemplate.exchange(
            url + "/v1/persons/" + personId,
            HttpMethod.PUT,
            entity,
            IndividualWriteResponseDto.class
        ).getBody().getId();
    }

    public ResponseEntity delete(String personId) {
        var headers = setUpAdminToken();
        var entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url + "/v1/persons/" + personId,
            HttpMethod.DELETE,
            entity,
            ResponseEntity.class
        );
    }
}
