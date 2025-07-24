package com.example.transaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnTransformer;

import com.example.transaction.entity.base.BaseEntity;
import com.example.transaction.entity.type.TransactionStatus;
import com.example.transaction.entity.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@Entity
@Table(name = "transactions", schema = "transaction")
public class Transaction extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @ColumnTransformer(write = "?::transaction.transaction_type")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @ColumnTransformer(write = "?::transaction.transaction_status")
    private TransactionStatus status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "fee")
    private BigDecimal fee;

    @ManyToOne
    @JoinColumn(name = "target_wallet_id")
    private Wallet targetWallet;

    @JsonProperty("payment_method_id")
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "failure_reason")
    private String failureReason;

}