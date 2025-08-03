package com.example.service;

import feign.FeignException;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.example.exception.IndividualException;
import com.example.individual.dto.ConfirmRequest;
import com.example.individual.dto.InitRequest;
import com.example.individual.dto.TransactionConfirmResponse;
import com.example.individual.dto.TransactionDto;
import com.example.individual.dto.TransactionInitResponse;
import com.example.individual.dto.TransactionPageDto;
import com.example.individual.dto.TransactionStatusDto;
import com.example.individual.dto.TransactionTypeDto;
import com.example.mapper.TransactionMapper;
import com.example.transaction.api.TransactionApiClient;
import com.example.transaction.dto.ErrorResponse;
import com.example.util.JsonWrapper;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionApiClient transactionApiClient;
    private final TransactionMapper transactionMapper;
    private final JsonWrapper jsonWrapper;

    @WithSpan(value = "transactionService.findById")
    public Mono<TransactionDto> findById(UUID transactionId) {
        return Mono.fromCallable(() -> transactionApiClient.getStatus(transactionId.toString()))
            .map(dto -> transactionMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic());
    }

    @WithSpan(value = "transactionService.findAll")
    public Mono<TransactionPageDto> findAll(
        List<String> userIds,
        List<String> walletIds,
        List<TransactionTypeDto> types,
        List<TransactionStatusDto> statuses,
        OffsetDateTime dateFrom,
        OffsetDateTime dateTo,
        Integer offset,
        Integer limit
    ) {
        var typesRequest = ofNullable(types).stream()
            .flatMap(Collection::stream)
            .map(status -> com.example.transaction.dto.TransactionTypeDto.fromValue(status.name()))
            .toList();
        var statusesRequest = ofNullable(statuses).stream()
            .flatMap(Collection::stream)
            .map(status -> com.example.transaction.dto.TransactionStatusDto.fromValue(status.name()))
            .toList();
        return Mono.fromCallable(
                () -> transactionApiClient.findAll(userIds, walletIds, typesRequest, statusesRequest, dateFrom, dateTo, offset, limit))
            .map(dto -> transactionMapper.from(dto.getBody()))
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext(_ -> log.debug("Transactions findAll called"))
            .doOnError(this::handleError);
    }

    @WithSpan(value = "transactionService.confirm")
    public Mono<TransactionConfirmResponse> confirm(
        TransactionTypeDto type,
        Mono<ConfirmRequest> confirmRequest
    ) {
        return confirmRequest
            .doOnNext(request -> log.debug("Starting transaction confirming dto={}", request))
            .flatMap(request ->
                Mono.fromCallable(() -> transactionApiClient.confirm(transactionMapper.from(type), transactionMapper.from(type, request)))
                    .map(dto -> transactionMapper.from(dto.getBody()))
                    .subscribeOn(Schedulers.boundedElastic()))
            .doOnNext(response -> log.info("Transaction confirmed successfully id={}", response.getTransactionId()))
            .doOnError(this::handleError);
    }

    private void handleError(Throwable e) {
        ((FeignException) e).responseBody().ifPresent(
            byteBuffer -> {
                var errorResponse = jsonWrapper.read(byteBuffer.array(), ErrorResponse.class);
                log.error(errorResponse.getMessage());
                throw new IndividualException(errorResponse.getMessage());
            }
        );
    }

    @WithSpan(value = "transactionService.init")
    public Mono<TransactionInitResponse> init(
        TransactionTypeDto type,
        Mono<InitRequest> initRequest
    ) {
        return initRequest
            .flatMap(request ->
                Mono.fromCallable(() -> transactionApiClient.init(
                        com.example.transaction.dto.TransactionTypeDto.fromValue(type.name()),
                        transactionMapper.from(type, request)
                    ))
                    .map(dto -> transactionMapper.from(dto.getBody()))
                    .subscribeOn(Schedulers.boundedElastic()))
            .doOnError(this::handleError);
    }
}
