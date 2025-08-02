package com.example.config.web;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.example.exception.IndividualException;
import com.example.individual.dto.ConfirmRequest;
import com.example.individual.dto.DepositConfirmRequest;
import com.example.individual.dto.TransactionTypeDto;
import com.example.individual.dto.TransferConfirmRequest;
import com.example.individual.dto.WithdrawalConfirmRequest;
import com.example.util.EnumUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.web.reactive.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@Component
public class TransactionConfirmRequestHttpMessageReader implements HttpMessageReader<ConfirmRequest> {

    private static final String CONFIRM_REQUEST_TYPE_PATH_VARIABLE = "type";

    private final ObjectMapper objectMapper;

    public TransactionConfirmRequestHttpMessageReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<MediaType> getReadableMediaTypes() {
        return List.of(MediaType.APPLICATION_JSON);
    }

    @Override
    public boolean canRead(
        ResolvableType elementType,
        @Nullable
        MediaType mediaType
    ) {
        return ConfirmRequest.class.isAssignableFrom(elementType.toClass())
               && (mediaType == null || MediaType.APPLICATION_JSON.includes(mediaType));
    }

    @Override
    public Flux<ConfirmRequest> read(
        ResolvableType elementType,
        ReactiveHttpInputMessage message,
        Map<String, Object> hints
    ) {
        return readMono(elementType, message, hints).flux();
    }

    @Override
    public Mono<ConfirmRequest> readMono(
        ResolvableType elementType,
        ReactiveHttpInputMessage message,
        Map<String, Object> hints
    ) {
        return DataBufferUtils.join(message.getBody())
            .flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                DataBufferUtils.release(dataBuffer);

                var body = new String(bytes, StandardCharsets.UTF_8);

                var attributes = ((ServerHttpRequest) message).getAttributes();
                @SuppressWarnings("unchecked")
                var uriVariables = (Map<String, String>) attributes.getOrDefault(URI_TEMPLATE_VARIABLES_ATTRIBUTE, Map.of());

                var typeString = uriVariables.get(CONFIRM_REQUEST_TYPE_PATH_VARIABLE);
                if (typeString == null) {
                    return Mono.error(new IndividualException("Missing transaction type in URI"));
                }

                var type = EnumUtil.from(TransactionTypeDto.class, typeString, () -> new IndividualException("Unknown TransactionTypeDto: %s", typeString));

                Class<? extends ConfirmRequest> targetClass = switch (type) {
                    case DEPOSIT -> DepositConfirmRequest.class;
                    case WITHDRAWAL -> WithdrawalConfirmRequest.class;
                    case TRANSFER -> TransferConfirmRequest.class;
                };

                try {
                    return Mono.just(objectMapper.readValue(body, targetClass));
                } catch (Exception e) {
                    return Mono.error(new IndividualException("Failed to parse ConfirmRequest", e));
                }
            });
    }
}