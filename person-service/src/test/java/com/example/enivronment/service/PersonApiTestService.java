package com.example.enivronment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.dto.IndividualPageRequest;
import com.example.person.dto.IndividualDto;
import com.example.person.dto.IndividualPageDto;
import com.example.person.dto.IndividualWriteDto;
import com.example.person.dto.IndividualWriteResponseDto;

import static org.springframework.util.CollectionUtils.isEmpty;

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

    public IndividualPageDto findAll(IndividualPageRequest individualPageRequest) {
        var path = buildPath(url + "/v1/persons", individualPageRequest);

        return restTemplate.getForObject(path, IndividualPageDto.class);
    }

    private String buildPath(
        String path,
        IndividualPageRequest individualPageRequest
    ) {
        if (isEmpty(individualPageRequest.emails())) {
            return path;
        }

        var stringBuilder = new StringBuilder(path + "?");
        int count = 0;
        for (var email : individualPageRequest.emails()) {
            stringBuilder.append("email=").append(email);

            if (individualPageRequest.emails().size() > ++count) {
                stringBuilder.append("&");
            }
        }
        return stringBuilder.toString();
    }

    IndividualDto findBy(String url) {
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
