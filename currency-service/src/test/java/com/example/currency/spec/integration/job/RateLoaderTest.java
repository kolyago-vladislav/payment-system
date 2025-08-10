package com.example.currency.spec.integration.job;

import org.junit.jupiter.api.Test;

import com.example.currency.spec.LifecycleSpecification;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateLoaderTest extends LifecycleSpecification {

    @Test
    void shouldRecalculateRates() {
        //when
        loadRateJob.loadRates();
        var initialCount = conversionRateRepository.count();

        loadRateJob.loadRates();
        var recalculatedCount = conversionRateRepository.count();
        
        //then
        assertEquals(930, initialCount);
        assertEquals(1860, recalculatedCount);
    }

}
