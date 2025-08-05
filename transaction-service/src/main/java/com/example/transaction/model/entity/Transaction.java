package com.example.transaction.model.entity;

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
import lombok.experimental.Accessors;

import org.hibernate.annotations.ColumnTransformer;

import com.example.transaction.model.entity.base.BaseEntity;
import com.example.transaction.model.entity.type.TransactionStatus;
import com.example.transaction.model.entity.type.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "transactions", schema = "transaction")
public class Transaction extends BaseEntity {

    @Column(name = "user_id", updatable = false)
    private UUID userId;

    @Column(name = "wallet_id")
    private UUID walletId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_id", insertable = false, updatable = false)
    private Wallet wallet;

    @Column(name = "target_wallet_id")
    private UUID targetWalletId;

    @ManyToOne
    @JoinColumn(name = "target_wallet_id", insertable = false, updatable = false)
    private Wallet targetWallet;

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

    @JsonProperty("payment_method_id")
    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "failure_reason")
    private String failureReason;

}