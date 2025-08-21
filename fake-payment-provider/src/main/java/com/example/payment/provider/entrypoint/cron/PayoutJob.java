package com.example.payment.provider.entrypoint.cron;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.payment.provider.business.service.JobPayoutService;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Service
public class PayoutJob {

    private final JobPayoutService service;

    @Scheduled(cron = "0/10 * * * * *")
    public void sendPayout() {
        while (true) {
            var executed = service.runPayoutJob();

            if (!executed) {
                break;
            }
        }
    }
}
