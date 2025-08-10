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

    @Scheduled(cron = "*/30 * * * * *")
    @SchedulerLock(
        name = "LoadRateJob",
        lockAtMostFor = "10m",
        lockAtLeastFor = "5m"
    )
    public void loadRates() {
        frankfurterRateLoader.loadRates();
    }
}
