package com.example.payment.provider.enivronment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.payment.dto.PayoutDto;
import com.example.payment.dto.TransactionDto;
import com.example.payment.provider.enivronment.data.DtoCreator;

@Component
public class PaymentProviderApiTestService {

    private final RestTemplate restTemplate;
    private final String url;
    private final DtoCreator dtoCreator;

    public PaymentProviderApiTestService(
        RestTemplate restTemplate,
        @Value("${server.port}")
        int port,
        DtoCreator dtoCreator
    ) {
        this.restTemplate = restTemplate;
        this.url = "http://localhost:" + port + "/fake-payment-provider/v1";
        this.dtoCreator = dtoCreator;
    }

    public PayoutDto findPayoutDtoById(Long id) {
        var headers = new HttpHeaders();
        headers.setBasicAuth("transaction-service", "abc123");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url + "/payouts/" + id,
            HttpMethod.GET,
            entity,
            PayoutDto.class
        ).getBody();
    }

    public PayoutDto createPayout() {
        var headers = new HttpHeaders();
        headers.setBasicAuth("transaction-service", "abc123");
        var entity = new HttpEntity<>(dtoCreator.buildPayoutWriteDto(), headers);


        return restTemplate.exchange(
            url + "/payouts",
            HttpMethod.POST,
            entity,
            PayoutDto.class
        ).getBody();
    }

    public TransactionDto findTransactionDtoById(Long id) {
        var headers = new HttpHeaders();
        headers.setBasicAuth("transaction-service", "abc123");
        var entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
            url + "/transactions/" + id,
            HttpMethod.GET,
            entity,
            TransactionDto.class
        ).getBody();
    }

    public TransactionDto createTransactionDto() {
        var headers = new HttpHeaders();
        headers.setBasicAuth("transaction-service", "abc123");
        var entity = new HttpEntity<>(dtoCreator.buildTransactionWriteDto(), headers);


        return restTemplate.exchange(
            url + "/transactions",
            HttpMethod.POST,
            entity,
            TransactionDto.class
        ).getBody();
    }

    public TransactionDto updateTransactionStatus(Long id) {
        var headers = new HttpHeaders();
        headers.setBasicAuth("transaction-service", "abc123");
        var entity = new HttpEntity<>(dtoCreator.buildStatusUpdateDto(id), headers);


        return restTemplate.exchange(
            url + "/webhook/transaction",
            HttpMethod.POST,
            entity,
            TransactionDto.class
        ).getBody();
    }

    public TransactionDto updatePayoutStatus(Long id) {
        var headers = new HttpHeaders();
        headers.setBasicAuth("transaction-service", "abc123");
        var entity = new HttpEntity<>(dtoCreator.buildStatusUpdateDto(id), headers);


        return restTemplate.exchange(
            url + "/webhook/payout",
            HttpMethod.POST,
            entity,
            TransactionDto.class
        ).getBody();
    }

}
