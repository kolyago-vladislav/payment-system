package com.example.currency.entrypoint.cron;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.currency.business.service.FrankfurterRateLoader;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadRateJob {

    private final FrankfurterRateLoader frankfurterRateLoader;

    @Scheduled(cron = "${currency-service.cron.load-rate-job.periodicity}")
    @SchedulerLock(
        name = "${currency-service.cron.load-rate-job.name}",
        lockAtLeastFor = "${currency-service.cron.load-rate-job.lock-minimum}"
    )
    public void loadRates() {
        frankfurterRateLoader.loadRates();
    }
}
