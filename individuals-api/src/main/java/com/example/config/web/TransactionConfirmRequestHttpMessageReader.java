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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.example.exception.IndividualException;
import com.example.individual.dto.ConfirmRequest;
import com.example.individual.dto.DepositConfirmRequest;
import com.example.individual.dto.TransactionTypeDto;
import com.example.individual.dto.TransferConfirmRequest;
import com.example.individual.dto.WithdrawalConfirmRequest;
import com.example.util.EnumUtil;
import com.example.util.JsonWrapper;

import static org.springframework.web.reactive.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

@Component
@RequiredArgsConstructor
public class TransactionConfirmRequestHttpMessageReader implements HttpMessageReader<ConfirmRequest> {

    private static final String CONFIRM_REQUEST_TYPE_PATH_VARIABLE = "type";

    private final JsonWrapper jsonWrapper;

    @Override
    public @NonNull List<MediaType> getReadableMediaTypes() {
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
    public @NonNull Flux<ConfirmRequest> read(
        @NonNull ResolvableType elementType,
        @NonNull ReactiveHttpInputMessage message,
        @NonNull Map<String, Object> hints
    ) {
        return readMono(elementType, message, hints).flux();
    }

    @Override
    public @NonNull Mono<ConfirmRequest> readMono(
        @NonNull ResolvableType elementType,
        @NonNull ReactiveHttpInputMessage message,
        @NonNull Map<String, Object> hints
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

                var type = uriVariables.get(CONFIRM_REQUEST_TYPE_PATH_VARIABLE);
                if (type == null) {
                    return Mono.error(new IndividualException("Missing transaction type in URI"));
                }

                var typeDto = EnumUtil.from(
                    TransactionTypeDto.class,
                    type,
                    () -> new IndividualException("Unknown %s: %s", TransactionTypeDto.class, type));

                var targetClass = switch (typeDto) {
                    case DEPOSIT -> DepositConfirmRequest.class;
                    case WITHDRAWAL -> WithdrawalConfirmRequest.class;
                    case TRANSFER -> TransferConfirmRequest.class;
                };

                return Mono.just(jsonWrapper.read(body, targetClass));
            });
    }
}