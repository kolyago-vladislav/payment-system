package com.example.payment.provider.core.config.property;

import java.time.temporal.ChronoUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("external-service.webhook")
public record BackoffProperty(
    double failureRateThreshold,
    int maxRetryCount,
    ChronoUnit backoffTimeUnit
) {

}
