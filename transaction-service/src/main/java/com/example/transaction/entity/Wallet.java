package com.example.transaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import com.example.transaction.dto.TransactionStatus;
import com.example.transaction.entity.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "wallets", schema = "transaction")
public class Wallet extends BaseEntity {

    @Size(max = 32)
    @Column(name = "name", length = 32)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_type_id")
    private WalletType walletType;
    
    @Column(name = "user_id")
    private UUID userid;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "archived_at")
    private Instant archived;
}