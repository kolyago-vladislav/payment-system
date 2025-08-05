package com.example.transaction.core.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import com.example.transaction.business.service.outbox.event.base.OutboxEventHandler;
import com.example.transaction.business.service.transaction.confirm.base.ConfirmRequestHandler;
import com.example.transaction.business.service.transaction.init.base.InitRequestHandler;
import com.example.transaction.model.entity.type.TransactionType;

@Configuration
public class AppConfig {

    @Bean
    public DataSource shardingSphereDataSource(Environment env) throws Exception {
        var resource = new ClassPathResource("META-INF/sharding.yml");
        String rawYaml;
        try (InputStream inputStream = resource.getInputStream()) {
            rawYaml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }


        rawYaml = rawYaml
            .replace("${TRANSACTION_POSTGRES_0_HOST}", env.getProperty("TRANSACTION_POSTGRES_0_HOST", "localhost"))
            .replace("${TRANSACTION_POSTGRES_1_HOST}", env.getProperty("TRANSACTION_POSTGRES_1_HOST", "localhost"));

        return YamlShardingSphereDataSourceFactory.createDataSource(rawYaml.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneOffset.UTC);
    }

    @Bean
    public Map<TransactionType, InitRequestHandler> initRequestHandlers(List<InitRequestHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(InitRequestHandler::getType, Function.identity()));
    }

    @Bean
    public Map<TransactionType, ConfirmRequestHandler> confirmRequestHandlers(List<ConfirmRequestHandler> handlers) {
        return handlers.stream()
            .collect(Collectors.toMap(ConfirmRequestHandler::getType, Function.identity()));
    }

    @Bean
    public Map<TransactionType, OutboxEventHandler<?>> outboxEventHandlers(List<OutboxEventHandler<?>> handlers) {
        return handlers.stream()
            .collect(Collectors.toMap(OutboxEventHandler::getOutboxEventType, Function.identity()));
    }
}
