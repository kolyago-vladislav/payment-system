package com.example.enivronment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.person.dto.IndividualDto;
import com.example.person.dto.IndividualWriteDto;
import com.example.person.dto.IndividualWriteResponseDto;

@Component
public class PersonApiTestService {

    private final RestTemplate restTemplate;
    private final String url;

    public PersonApiTestService(
        RestTemplate restTemplate,
        @Value("${server.port}")
        int port
    ) {
        this.restTemplate = restTemplate;
        this.url = "http://localhost:" + port + "/person";
    }

    public IndividualDto findById(String personId) {
        return findBy(url + "/v1/persons/" + personId);
    }

    public IndividualDto findByEmail(String email) {
        return findBy(url + "/v1/persons/email/" + email);
    }

    private IndividualDto findBy(String url) {
        return restTemplate.getForObject(url, IndividualDto.class);
    }

    public String register(IndividualWriteDto individualWriteDto) {
        var entity = new HttpEntity<>(individualWriteDto);

        return restTemplate.exchange(
            url + "/v1/persons",
            HttpMethod.POST,
            entity,
            IndividualWriteResponseDto.class
        ).getBody().getId();
    }

    public String update(
        String personId,
        IndividualWriteDto individualWriteDto
    ) {
        var entity = new HttpEntity<>(individualWriteDto);

        return restTemplate.exchange(
            url + "/v1/persons/" + personId,
            HttpMethod.PUT,
            entity,
            IndividualWriteResponseDto.class
        ).getBody().getId();
    }

    public ResponseEntity delete(String personId) {
        return restTemplate.exchange(
            url + "/v1/persons/" + personId,
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            ResponseEntity.class
        );
    }
}
