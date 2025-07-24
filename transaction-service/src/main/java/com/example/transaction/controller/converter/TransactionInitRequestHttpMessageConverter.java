package com.example.transaction.controller.converter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import com.example.transaction.dto.DepositInitRequest;
import com.example.transaction.dto.InitRequest;
import com.example.transaction.dto.TransferInitRequest;
import com.example.transaction.dto.WithdrawalInitRequest;
import com.example.transaction.entity.type.TransactionType;
import com.example.transaction.exception.TransactionServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Optional.ofNullable;

@Component
public class TransactionInitRequestHttpMessageConverter extends AbstractHttpMessageConverter<InitRequest> {

    private static final String INIT_REQUEST_TYPE_PATH_VARIABLE = "type";

    private final ObjectMapper mapper;

    public TransactionInitRequestHttpMessageConverter(ObjectMapper mapper) {
        super(MediaType.APPLICATION_JSON);
        this.mapper = mapper;
    }

    @Override
    protected @NonNull InitRequest readInternal(
        @NonNull
        Class<? extends InitRequest> clazz,
        @NonNull
        HttpInputMessage inputMessage
    ) throws IOException, HttpMessageNotReadableException {
        var type = extractTransactionTypeFromPath();
        var body = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);

        var targetClass = switch (type) {
            case DEPOSIT -> DepositInitRequest.class;
            case WITHDRAWAL -> WithdrawalInitRequest.class;
            case TRANSFER -> TransferInitRequest.class;
        };

        return mapper.readValue(body, targetClass);
    }

    @SuppressWarnings("unchecked")
    private TransactionType extractTransactionTypeFromPath() {
        return ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .map(ServletRequestAttributes::getRequest)
            .map(request -> (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
            .map(uriVars -> uriVars.get(INIT_REQUEST_TYPE_PATH_VARIABLE))
            .map(TransactionType::from)
            .orElseThrow(() -> new TransactionServiceException("Cannot extract transaction type from URI variables"));
    }

    @Override
    protected boolean supports(
        @NonNull
        Class<?> clazz
    ) {
        return InitRequest.class.isAssignableFrom(clazz);
    }

    @Override
    protected void writeInternal(
        @NonNull
        InitRequest t,
        HttpOutputMessage outputMessage
    ) throws IOException {
        mapper.writeValue(outputMessage.getBody(), t);
    }
}
