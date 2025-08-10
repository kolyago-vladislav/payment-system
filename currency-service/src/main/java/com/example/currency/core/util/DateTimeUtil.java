package com.example.currency.core.util;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DateTimeUtil {

    private final Clock clock;

    public Instant now() {
        return clock.instant();
    }

    public OffsetDateTime to(Instant instant) {
        if (instant == null) {
            return null;
        }

        return instant.atOffset(ZoneOffset.UTC);
    }

    public Instant to(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }

        return offsetDateTime.toInstant();
    }
}
