package com.example.payment.provider.business.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.payment.provider.dto.StatusUpdateDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookClient {

    private final RestTemplate restTemplate;

    public void sendPayoutWebhook(StatusUpdateDto body) {
        restTemplate.postForObject("http://localhost:8095/fake-payment-provider/v1/webhook/payout", body, Void.class);
    }

    public void sendTransactionWebhook(StatusUpdateDto body) {
        restTemplate.postForObject("http://localhost:8095/fake-payment-provider/v1/webhook/transaction", body, Void.class);
    }
}
