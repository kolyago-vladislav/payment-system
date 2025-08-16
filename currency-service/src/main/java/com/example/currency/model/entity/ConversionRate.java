package com.example.currency.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

import com.example.currency.model.entity.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "conversion_rates", schema = "currency")
public class ConversionRate extends BaseEntity {

    @Column(name = "source_code")
    private String sourceCode;

    @Column(name = "target_code")
    private String targetCode;

    @Column(name = "rate_begin_time")
    private Instant rateBeginTime;

    @Column(name = "rate_end_time")
    private Instant rateEndTime;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "provider_id")
    private String providerId;

}