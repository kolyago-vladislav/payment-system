package com.example.config.web;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.exception.IndividualException;
import com.example.individual.dto.DepositInitRequest;
import com.example.individual.dto.InitRequest;
import com.example.individual.dto.TransactionTypeDto;
import com.example.individual.dto.TransferInitRequest;
import com.example.individual.dto.WithdrawalInitRequest;
import com.example.util.EnumUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.web.reactive.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@Component
@RequiredArgsConstructor
public class TransactionInitRequestHttpMessageReader implements HttpMessageReader<InitRequest> {

    private static final String INIT_REQUEST_TYPE_PATH_VARIABLE = "type";

    private final ObjectMapper objectMapper;

    @Override
    public List<MediaType> getReadableMediaTypes() {
        return List.of(MediaType.APPLICATION_JSON);
    }

    @Override
    public boolean canRead(
        ResolvableType elementType,
        @Nullable MediaType mediaType
    ) {
        return InitRequest.class.isAssignableFrom(elementType.toClass())
               && (mediaType == null || MediaType.APPLICATION_JSON.includes(mediaType));
    }

    @Override
    public Flux<InitRequest> read(
        ResolvableType elementType,
        ReactiveHttpInputMessage inputMessage,
        Map<String, Object> hints
    ) {
        return readMono(elementType, inputMessage, hints).flux();
    }

    @Override
    public Mono<InitRequest> readMono(
        ResolvableType elementType,
        ReactiveHttpInputMessage inputMessage,
        Map<String, Object> hints
    ) {
        ServerWebExchange exchange = (ServerWebExchange) hints.get(ServerWebExchange.class.getName());

        if (exchange == null) {
            return Mono.error(new IndividualException("Missing ServerWebExchange in hints"));
        }

        return DataBufferUtils.join(inputMessage.getBody())
            .flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);

                String body = new String(bytes, StandardCharsets.UTF_8);

                Map<String, String> uriVariables = exchange.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                if (uriVariables == null || !uriVariables.containsKey(INIT_REQUEST_TYPE_PATH_VARIABLE)) {
                    return Mono.error(new IndividualException("Missing 'type' path variable"));
                }

                String typeString = uriVariables.get(INIT_REQUEST_TYPE_PATH_VARIABLE);

                TransactionTypeDto type = EnumUtil.from(TransactionTypeDto.class, typeString,
                    () -> new IndividualException("Unknown TransactionTypeDto: %s", typeString));

                Class<? extends InitRequest> targetClass = switch (type) {
                    case DEPOSIT -> DepositInitRequest.class;
                    case WITHDRAWAL -> WithdrawalInitRequest.class;
                    case TRANSFER -> TransferInitRequest.class;
                };

                try {
                    return Mono.just(objectMapper.readValue(body, targetClass));
                } catch (Exception e) {
                    return Mono.error(new IndividualException("Failed to parse InitRequest", e));
                }
            });
    }
}