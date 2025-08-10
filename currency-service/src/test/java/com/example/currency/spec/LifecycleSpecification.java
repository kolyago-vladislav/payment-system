package com.example.currency.spec;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.example.currency.CurrencyServiceApplication;
import com.example.currency.business.repository.ConversionRateRepository;
import com.example.currency.business.service.FrankfurterRateLoader;
import com.example.currency.enivronment.config.testcontainer.TestcontainersApplicationContextInitializer;
import com.example.currency.enivronment.data.DtoCreator;
import com.example.currency.enivronment.service.CurrencyApiTestService;
import com.example.currency.enivronment.service.DatabaseTestService;

@ActiveProfiles("test")
@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = CurrencyServiceApplication.class
)
@ContextConfiguration(initializers = TestcontainersApplicationContextInitializer.class)
@Setter(onMethod_ = {@Autowired})
public abstract class LifecycleSpecification {

    public static final String PERSON_EMAIL = "newuser@gmail.com";

    protected DtoCreator dtoCreator;
    protected CurrencyApiTestService currencyApiTestService;
    protected DatabaseTestService databaseTestService;
    protected FrankfurterRateLoader loadRateJob;
    protected ConversionRateRepository conversionRateRepository;

    @AfterEach
    public void clear() {
        databaseTestService.truncateAllTables();
    }
}
