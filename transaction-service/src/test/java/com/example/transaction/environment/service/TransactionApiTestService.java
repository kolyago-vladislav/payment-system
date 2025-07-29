package com.example.transaction.environment.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.jetbrains.annotations.Nullable;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.TransactionConfirmResponse;
import com.example.transaction.dto.TransactionDto;
import com.example.transaction.dto.TransactionPageDto;
import com.example.transaction.model.dto.TransactionPageRequestDto;

import static java.util.Optional.ofNullable;

@Service
public class TransactionApiTestService {
    private final RestTemplate restTemplate;
    private String url;

    public TransactionApiTestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TransactionConfirmResponse confirmDeposit(ConfirmRequest dto) {
        return confirm(dto, "/v1/transactions/deposit/confirm");
    }

    public TransactionConfirmResponse confirmWithdrawal(ConfirmRequest dto) {
        return confirm(dto, "/v1/transactions/withdrawal/confirm");
    }

    public TransactionConfirmResponse confirmTransfer(ConfirmRequest dto) {
        return confirm(dto, "/v1/transactions/transfer/confirm");
    }

    private @Nullable TransactionConfirmResponse confirm(ConfirmRequest dto, String path) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
            url + path,
            HttpMethod.POST,
            entity,
            TransactionConfirmResponse.class
        ).getBody();
    }

    public TransactionPageDto findAll(TransactionPageRequestDto dto) {
        var path = buildPath(url + "/v1/transactions", dto);

        return restTemplate.getForObject(path, TransactionPageDto.class);
    }

    private String buildPath(String path, TransactionPageRequestDto dto) {
        var params = new ArrayList<String>();

        ofNullable(dto.userIds()).ifPresent(ids -> ids.forEach(id -> params.add("userId=" + encode(id))));
        ofNullable(dto.walletIds()).ifPresent(ids -> ids.forEach(id -> params.add("walletId=" + encode(id))));
        ofNullable(dto.types()).ifPresent(ids -> ids.forEach(id -> params.add("type=" + encode(id.getValue()))));
        ofNullable(dto.statuses()).ifPresent(ids -> ids.forEach(id -> params.add("status=" + encode(id.getValue()))));

        if (dto.dateFrom() != null) {
            params.add("dateFrom=" + encode(dto.dateFrom().toString()));
        }
        if (dto.dateTo() != null) {
            params.add("dateTo=" + encode(dto.dateTo().toString()));
        }
        if (dto.limit() != null) {
            params.add("limit=" + dto.limit());
        }

        return path + "?" + String.join("&", params);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public TransactionDto findByTransactionId(String id) {
        return restTemplate.getForObject(url + "/v1/transactions/%s/status".formatted(id), TransactionDto.class);
    }

    public void setUrl(int port) {
        this.url = "http://localhost:" + port + "/transaction";
    }
}
