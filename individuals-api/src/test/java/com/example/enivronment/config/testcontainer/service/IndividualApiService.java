package com.example.enivronment.config.testcontainer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.TokenResponse;
import com.example.individual.dto.UserInfoResponse;
import com.example.individual.dto.UserLoginRequest;

import static org.springframework.http.HttpMethod.GET;

@Component
public class IndividualApiService {

    private final RestTemplate restTemplate;
    private final String url;

    public IndividualApiService(RestTemplate restTemplate, @Value("${server.port}") int  port) {
        this.restTemplate = restTemplate;
        this.url = "http://localhost:" + port;
    }

    public TokenResponse register(IndividualWriteDto request) {
        return restTemplate.postForObject(url + "/v1/auth/registration", request, TokenResponse.class);
    }


    public TokenResponse login(UserLoginRequest request) {
        return restTemplate.postForObject(url + "/v1/auth/login", request, TokenResponse.class);
    }

    public UserInfoResponse getMe(String accessToken) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        var response = restTemplate.exchange(url + "/v1/auth/me", GET, new HttpEntity<>(headers), UserInfoResponse.class);

        return response.getBody();
    }
}
