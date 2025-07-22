CREATE TABLE transaction.wallet_types
(
    id            uuid PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    name          VARCHAR(32)                 NOT NULL,
    currency_code VARCHAR(3)                  NOT NULL,

    status        VARCHAR(18)                 NOT NULL,
    archived_at   TIMESTAMP WITHOUT TIME ZONE,
    user_type     VARCHAR(15),
    creator       VARCHAR(255),
    modifier      VARCHAR(255),

    CONSTRAINT uc_wallet_type_name UNIQUE (name)
);


CREATE TYPE transaction.wallet_status AS ENUM ('ACTIVE', 'ARCHIVED', 'BLOCKED');
CREATE TABLE transaction.wallets
(
    id             uuid PRIMARY KEY                     DEFAULT uuid_generate_v4(),
    created        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    name           VARCHAR(32)                 NOT NULL,
    wallet_type_id uuid                        NOT NULL,
    user_id        uuid                        NOT NULL,
    status         transaction.wallet_status   NOT NULL,
    balance        DECIMAL                     NOT NULL DEFAULT 0.0,
    archived_at    TIMESTAMP WITHOUT TIME ZONE
);
ALTER TABLE transaction.wallets
    ADD CONSTRAINT fk_wallet_wallet_type
        FOREIGN KEY (wallet_type_id)
            REFERENCES transaction.wallet_types (id);
ALTER TABLE transaction.wallets
    ADD CONSTRAINT chk_archived_at_required
        CHECK (status != 'ARCHIVED'::transaction.wallet_status OR archived_at IS NOT NULL);


CREATE TYPE transaction.payment_type AS ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER');
CREATE TYPE transaction.transaction_status AS ENUM ('FAILED', 'CONFIRMED', 'PENDING');
CREATE TABLE transaction.transactions
(
    id                uuid PRIMARY KEY                        DEFAULT uuid_generate_v4(),
    created           TIMESTAMP WITHOUT TIME ZONE    NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated           TIMESTAMP WITHOUT TIME ZONE    NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    user_id           uuid                           NOT NULL,
    wallet_id         uuid                           NOT NULL,
    amount            DECIMAL                        NOT NULL DEFAULT 0.0,
    type              transaction.payment_type       NOT NULL,
    status            transaction.transaction_status NOT NULL,
    comment           VARCHAR(256),
    fee               DECIMAL                        NOT NULL DEFAULT 0.0,
    target_wallet_id  uuid,   -- для transfer
    payment_method_id BIGINT, -- для deposit/withdrawal
    failure_reason    VARCHAR(256)
);
ALTER TABLE transaction.transactions
    ADD CONSTRAINT fk_transaction_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES transaction.wallets (id);
ALTER TABLE transaction.transactions
    ADD CONSTRAINT fk_transaction_target_wallet
        FOREIGN KEY (target_wallet_id)
            REFERENCES transaction.wallets (id);

ALTER TABLE transaction.transactions
    ADD CONSTRAINT chk_payment_method_id_required
        CHECK (
            (type IN ('DEPOSIT', 'WITHDRAWAL') AND payment_method_id IS NOT NULL)
                OR
            (type = 'TRANSFER' AND payment_method_id IS NULL)
            );

ALTER TABLE transaction.transactions
    ADD CONSTRAINT chk_target_wallet_id_required
        CHECK (
            (type = 'TRANSFER' AND target_wallet_id IS NOT NULL)
                OR
            (type IN ('DEPOSIT', 'WITHDRAWAL') AND target_wallet_id IS NULL)
            );

ALTER TABLE transaction.transactions
    ADD CONSTRAINT chk_failure_reason_required
        CHECK (
            (status = 'FAILED' AND failure_reason IS NOT NULL)
                OR
            (status <> 'FAILED' AND failure_reason IS NULL)
            );