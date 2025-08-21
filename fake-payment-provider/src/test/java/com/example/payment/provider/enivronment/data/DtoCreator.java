package com.example.payment.provider.enivronment.data;

import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.example.payment.dto.PayoutDto;
import com.example.payment.dto.PayoutWriteDto;
import com.example.payment.dto.StatusUpdateDto;
import com.example.payment.dto.TransactionDto;
import com.example.payment.dto.TransactionWriteDto;
import com.example.payment.provider.model.dto.Status;

import static com.example.payment.provider.integration.LifecycleSpecification.DEFAULT_LOCAL_DATE_TIME;

@Component
public class DtoCreator {

    public PayoutDto buildPayoutDto() {
        return new PayoutDto()
            .id(1L)
            .merchantId("1")
            .amount(100.03)
            .currency("USD")
            .status("PENDING")
            .createdAt(DEFAULT_LOCAL_DATE_TIME.atOffset(ZoneOffset.UTC))
            .updatedAt(DEFAULT_LOCAL_DATE_TIME.atOffset(ZoneOffset.UTC))
            .externalId("EXTERNAL_ID_1");
    }

    public PayoutWriteDto buildPayoutWriteDto() {
        return new PayoutWriteDto()
            .amount(100.03)
            .currency("USD");
    }

    public TransactionDto buildTransactionDto() {
        return new TransactionDto()
            .id(1L)
            .merchantId("1")
            .amount(100.03)
            .currency("USD")
            .status("PENDING")
            .method("CASHLESS")
            .description("PAY in")
            .createdAt(DEFAULT_LOCAL_DATE_TIME.atOffset(ZoneOffset.UTC))
            .updatedAt(DEFAULT_LOCAL_DATE_TIME.atOffset(ZoneOffset.UTC))
            .externalId("EXTERNAL_ID_1");
    }

    public TransactionWriteDto buildTransactionWriteDto() {
        return new TransactionWriteDto()
            .amount(100.03)
            .currency("USD")
            .description("PAY in")
            .method("CASHLESS");
    }

    public StatusUpdateDto buildStatusUpdateDto(Long id) {
        return new StatusUpdateDto()
            .id(id)
            .status(Status.SUCCESS.name());
    }
}
