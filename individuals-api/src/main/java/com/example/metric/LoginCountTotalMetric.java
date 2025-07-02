package com.example.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.stereotype.Component;

@Component
public class LoginCountTotalMetric {

    public static final String LOGIN_COUNT_TOTAL_METRIC_NAME = "individual_app_login_count_total";

    private final Counter counter;

    public LoginCountTotalMetric(MeterRegistry registry) {
        counter = Counter.builder(LOGIN_COUNT_TOTAL_METRIC_NAME).register(registry);
    }

    public void incrementCounter() {
        counter.increment();
    }
}
