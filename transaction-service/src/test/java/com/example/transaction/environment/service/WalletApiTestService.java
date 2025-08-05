package com.example.transaction.environment.service;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.transaction.business.repository.WalletRepository;
import com.example.transaction.dto.WalletDto;
import com.example.transaction.dto.WalletPageDto;
import com.example.transaction.dto.WalletWriteDto;
import com.example.transaction.dto.WalletWriteResponseDto;

@Service
@RequiredArgsConstructor
public class WalletApiTestService {

    private final RestTemplate restTemplate;
    private final WalletRepository walletRepository;
    private String url;


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

        var walletId = restTemplate.exchange(
            url + "/v1/wallets",
            HttpMethod.POST,
            entity,
            WalletWriteResponseDto.class
        ).getBody().getId();

        var wallet = walletRepository.findById(UUID.fromString(walletId)).get();
        wallet.setBalance(BigDecimal.valueOf(150));
        walletRepository.save(wallet);
        return walletId;
    }

    WalletDto findBy(String url) {
        return restTemplate.getForObject(url, WalletDto.class);
    }

    public void setUrl(int port) {
        this.url = "http://localhost:" + port + "/transaction";
    }
}
