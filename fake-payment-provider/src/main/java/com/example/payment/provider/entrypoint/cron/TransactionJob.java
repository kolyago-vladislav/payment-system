package com.example.payment.provider.entrypoint.cron;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.payment.provider.business.service.JobTransactionService;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Service
public class TransactionJob {

    private final JobTransactionService service;

    @Scheduled(cron = "0/10 * * * * *")
    public void sendPayout() {
        while (true) {
            var executed = service.runTransactionJob();

            if (!executed) {
                break;
            }
        }
    }
}
