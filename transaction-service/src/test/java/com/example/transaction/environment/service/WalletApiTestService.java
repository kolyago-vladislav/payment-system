package com.example.transaction.environment.service;

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
    private String url;



    public WalletApiTestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WalletDto findById(String id) {
        return findBy(url + "/v1/wallets/" + id);
    }

    public WalletPageDto findByUserId(String id) {
        return restTemplate.getForObject(url + "/v1/wallets/user/" + id, WalletPageDto.class);
    }

    public String register(WalletWriteDto dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WalletWriteDto> entity = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
            url + "/v1/wallets",
            HttpMethod.POST,
            entity,
            WalletWriteResponseDto.class
        ).getBody().getId();
    }

    WalletDto findBy(String url) {
        return restTemplate.getForObject(url, WalletDto.class);
    }

    public void setUrl(int port) {
        this.url = "http://localhost:" + port + "/transaction";
    }
}
