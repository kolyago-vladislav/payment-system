package com.example.transaction.model.entity;

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
import lombok.experimental.Accessors;

import org.hibernate.annotations.ColumnTransformer;

import com.example.transaction.model.entity.base.BaseEntity;
import com.example.transaction.model.entity.type.WalletStatus;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "wallets", schema = "transaction")
public class Wallet extends BaseEntity {

    @Size(max = 32)
    @Column(name = "name", length = 32)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_type_id")
    private WalletType walletType;
    
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::transaction.wallet_status")
    private WalletStatus status;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "archived_at")
    private Instant archived;
}