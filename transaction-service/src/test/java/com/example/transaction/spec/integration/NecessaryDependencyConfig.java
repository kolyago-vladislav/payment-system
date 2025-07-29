package com.example.transaction.spec.integration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.annotation.PostConstruct;

import lombok.Setter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.example.transaction.core.util.JsonWrapper;
import com.example.transaction.environment.data.DtoCreator;
import com.example.transaction.environment.service.DatabaseTestService;
import com.example.transaction.environment.service.OutboxEventTestService;
import com.example.transaction.environment.service.TransactionApiTestService;
import com.example.transaction.environment.service.WalletApiTestService;

@Setter(onMethod_ = {@Autowired})
public abstract class NecessaryDependencyConfig extends LifecycleSpecification {

    protected DtoCreator dtoCreator;
    protected OutboxEventTestService outboxEventTestService;
    protected WalletApiTestService walletApiTestService;
    protected TransactionApiTestService transactionApiTestService;
    protected DatabaseTestService databaseTestService;
    protected JsonWrapper jsonWrapper;

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

    @BeforeEach
    public void setup() {
        transactionApiTestService.setUrl(getPort());
        walletApiTestService.setUrl(getPort());
    }
}
