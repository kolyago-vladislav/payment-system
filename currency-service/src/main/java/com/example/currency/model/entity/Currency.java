package com.example.currency.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.example.currency.model.entity.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "currencies", schema = "currency")
public class Currency extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "iso_code")
    private Integer isoCode;

    @Column(name = "description")
    private String description;

    @Column(name = "symbol")
    private String symbol;

}