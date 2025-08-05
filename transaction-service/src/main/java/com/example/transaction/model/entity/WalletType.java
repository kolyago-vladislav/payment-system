package com.example.transaction.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

import com.example.transaction.model.entity.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "wallet_types", schema = "transaction")
public class WalletType  extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "status")
    private String status;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "creator")
    private String creator;

    @Column(name = "modifier")
    private String modifier;
}