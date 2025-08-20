package com.example.payment.provider.integration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.annotation.PostConstruct;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.example.payment.provider.PaymentProviderApplication;
import com.example.payment.provider.enivronment.config.testcontainer.TestcontainersApplicationContextInitializer;
import com.example.payment.provider.enivronment.data.DtoCreator;
import com.example.payment.provider.enivronment.service.DatabaseTestService;
import com.example.payment.provider.enivronment.service.PaymentProviderApiTestService;

@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    classes = PaymentProviderApplication.class
)
@ContextConfiguration(initializers = TestcontainersApplicationContextInitializer.class)
@Setter(onMethod_ = {@Autowired})
public abstract class LifecycleSpecification {

    protected DtoCreator dtoCreator;
    protected PaymentProviderApiTestService service;
    protected DatabaseTestService databaseTestService;

    @MockitoSpyBean
    protected Clock clock;

    public static final LocalDateTime DEFAULT_LOCAL_DATE_TIME = LocalDateTime.of(2024, 10, 2, 7, 4, 3);

    @PostConstruct
    void init() {
        Mockito
            .doReturn(DEFAULT_LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC))
            .when(clock).instant();
    }

    @AfterEach
    public void clear() {
        databaseTestService.truncateAllTables();
    }
}
