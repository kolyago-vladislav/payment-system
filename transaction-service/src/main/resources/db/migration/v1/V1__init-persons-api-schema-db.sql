CREATE TABLE transaction.wallet_types
(
    uid           uuid PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    active        BOOLEAN                     NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    name          VARCHAR(32)                 NOT NULL,
    currency_code VARCHAR(3)                  NOT NULL
);



CREATE TABLE transaction.wallets
(
    uid             uuid PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    active          BOOLEAN                     NOT NULL DEFAULT TRUE,
    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    name            VARCHAR(32)                 NOT NULL,
    wallet_type_uid uuid                        NOT NULL,
    user_uid        uuid                        NOT NULL,
    balance         DECIMAL                     NOT NULL DEFAULT 0.0
);
ALTER TABLE transaction.wallets
    ADD CONSTRAINT fk_wallet_wallet_type
        FOREIGN KEY (wallet_type_uid)
            REFERENCES transaction.wallet_types (uid);



CREATE TYPE transaction.payment_type AS ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER');
CREATE TYPE transaction.transaction_status AS ENUM ('FAILED', 'CONFIRMED', 'PENDING');
CREATE TABLE transaction.transactions
(
    uid               uuid PRIMARY KEY                        DEFAULT uuid_generate_v4(),
    created           TIMESTAMP WITHOUT TIME ZONE    NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated           TIMESTAMP WITHOUT TIME ZONE    NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    user_uid          uuid                           NOT NULL,
    wallet_uid        uuid                           NOT NULL,
    amount            DECIMAL                        NOT NULL DEFAULT 0.0,
    type              transaction.payment_type       NOT NULL,
    status            transaction.transaction_status NOT NULL,
    fee               DECIMAL                        NOT NULL DEFAULT 0.0,
    target_wallet_uid uuid,   -- для transfer
    payment_method_id BIGINT, -- для deposit/withdrawal
    failure_reason    VARCHAR(256)
);
ALTER TABLE transaction.transactions
    ADD CONSTRAINT fk_transaction_wallet
        FOREIGN KEY (wallet_uid)
            REFERENCES transaction.wallets (uid);
ALTER TABLE transaction.transactions
    ADD CONSTRAINT fk_transaction_target_wallet
        FOREIGN KEY (target_wallet_uid)
            REFERENCES transaction.wallets (uid);

ALTER TABLE transaction.transactions
    ADD CONSTRAINT chk_payment_method_id_required
        CHECK (
            (type IN ('DEPOSIT', 'WITHDRAWAL') AND payment_method_id IS NOT NULL)
                OR
            (type = 'TRANSFER' AND payment_method_id IS NULL)
            );

ALTER TABLE transaction.transactions
    ADD CONSTRAINT chk_target_wallet_uid_required
        CHECK (
            (type = 'TRANSFER' AND target_wallet_uid IS NOT NULL)
                OR
            (type IN ('DEPOSIT', 'WITHDRAWAL') AND target_wallet_uid IS NULL)
            );

ALTER TABLE transaction.transactions
    ADD CONSTRAINT chk_failure_reason_required
        CHECK (
            (status = 'FAILED' AND failure_reason IS NOT NULL)
                OR
            (status <> 'FAILED' AND failure_reason IS NULL)
            );