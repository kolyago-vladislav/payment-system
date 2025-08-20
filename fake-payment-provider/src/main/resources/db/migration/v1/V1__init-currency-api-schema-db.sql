CREATE TABLE payment_provider.merchants
(
    id          SERIAL PRIMARY KEY,
    merchant_id VARCHAR(50)                 NOT NULL UNIQUE,
    secret_key  VARCHAR(255)                NOT NULL,
    name        VARCHAR(100),
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc')
);

CREATE TABLE payment_provider.transactions
(
    id          BIGSERIAL PRIMARY KEY,
    merchant_id INTEGER                     NOT NULL REFERENCES payment_provider.merchants (id),
    amount      NUMERIC(18, 2)              NOT NULL,
    currency    VARCHAR(3)                  NOT NULL,
    method      VARCHAR(50)                 NOT NULL,
    status      VARCHAR(20)                 NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    retry_count INTEGER                     NOT NULL DEFAULT 0,
    description VARCHAR(255),
    external_id VARCHAR(100)
);
CREATE INDEX idx_transactions_merchant_date ON payment_provider.transactions (merchant_id, created_at);

CREATE TABLE payment_provider.payouts
(
    id          BIGSERIAL PRIMARY KEY,
    merchant_id INTEGER                     NOT NULL REFERENCES payment_provider.merchants (id),
    amount      NUMERIC(18, 2)              NOT NULL,
    currency    VARCHAR(3)                  NOT NULL,
    status      VARCHAR(20)                 NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    retry_count INTEGER                     NOT NULL DEFAULT 0,
    external_id VARCHAR(100)
);
CREATE INDEX idx_payouts_merchant_date ON payment_provider.payouts (merchant_id, created_at);

CREATE TABLE payment_provider.webhooks
(
    id          BIGSERIAL PRIMARY KEY,
    event_type  VARCHAR(50)                 NOT NULL,
    entity_id   BIGINT                      NOT NULL,
    payload     jsonb,
    received_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc')
);
CREATE INDEX idx_webhooks_entity ON payment_provider.webhooks (event_type, entity_id);