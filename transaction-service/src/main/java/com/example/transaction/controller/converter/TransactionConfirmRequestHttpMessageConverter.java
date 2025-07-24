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

import com.example.transaction.dto.ConfirmRequest;
import com.example.transaction.dto.DepositConfirmRequest;
import com.example.transaction.dto.TransferConfirmRequest;
import com.example.transaction.dto.WithdrawalConfirmRequest;
import com.example.transaction.entity.type.TransactionType;
import com.example.transaction.exception.TransactionServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.Optional.ofNullable;

@Component
public class TransactionConfirmRequestHttpMessageConverter extends AbstractHttpMessageConverter<ConfirmRequest> {

    private static final String Confirm_REQUEST_TYPE_PATH_VARIABLE = "type";

    private final ObjectMapper mapper;

    public TransactionConfirmRequestHttpMessageConverter(ObjectMapper mapper) {
        super(MediaType.APPLICATION_JSON);
        this.mapper = mapper;
    }

    @Override
    protected @NonNull ConfirmRequest readInternal(
        @NonNull
        Class<? extends ConfirmRequest> clazz,
        @NonNull
        HttpInputMessage inputMessage
    ) throws IOException, HttpMessageNotReadableException {
        var type = extractTransactionTypeFromPath();
        var body = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);

        var targetClass = switch (type) {
            case DEPOSIT -> DepositConfirmRequest.class;
            case WITHDRAWAL -> WithdrawalConfirmRequest.class;
            case TRANSFER -> TransferConfirmRequest.class;
        };

        return mapper.readValue(body, targetClass);
    }

    @SuppressWarnings("unchecked")
    private TransactionType extractTransactionTypeFromPath() {
        return ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .map(ServletRequestAttributes::getRequest)
            .map(request -> (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
            .map(uriVars -> uriVars.get(Confirm_REQUEST_TYPE_PATH_VARIABLE))
            .map(TransactionType::from)
            .orElseThrow(() -> new TransactionServiceException("Cannot extract transaction type from URI variables"));
    }

    @Override
    protected boolean supports(
        @NonNull
        Class<?> clazz
    ) {
        return ConfirmRequest.class.isAssignableFrom(clazz);
    }

    @Override
    protected void writeInternal(
        @NonNull
        ConfirmRequest t,
        HttpOutputMessage outputMessage
    ) throws IOException {
        mapper.writeValue(outputMessage.getBody(), t);
    }
}
