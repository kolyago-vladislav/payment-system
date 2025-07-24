package com.example.transaction.core.util;

import java.time.Clock;
import java.time.Instant;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DateTimeUtil {

    private final Clock clock;

    public Instant now() {
        return clock.instant();
    }
}
