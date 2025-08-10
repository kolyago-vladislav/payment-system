CREATE TABLE currency.currencies
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  timestamptz NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at  timestamptz NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    active      BOOLEAN              DEFAULT TRUE,
    code        VARCHAR(3)  NOT NULL UNIQUE,
    iso_code    INTEGER     NOT NULL UNIQUE,
    description VARCHAR(64) NOT NULL,
    symbol      VARCHAR(3)
);

CREATE TABLE currency.rate_providers
(
    id          VARCHAR(32) PRIMARY KEY,
    created_at  timestamptz NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at  timestamptz NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    active      BOOLEAN              DEFAULT TRUE,
    name        VARCHAR(32) NOT NULL UNIQUE,
    description VARCHAR(256),
    priority    INTEGER     NOT NULL
);

CREATE TABLE currency.conversion_rates
(
    id              BIGSERIAL PRIMARY KEY,
    source_code     VARCHAR(3)                  NOT NULL REFERENCES currency.currencies (code),
    target_code     VARCHAR(3)                  NOT NULL REFERENCES currency.currencies (code),
    rate            NUMERIC                     NOT NULL,
    amount          int4                        NOT NULL,
    active          BOOLEAN                              DEFAULT TRUE,
    provider_id     VARCHAR(32) REFERENCES currency.rate_providers,
    rate_begin_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    rate_end_time   TIMESTAMP WITHOUT TIME ZONE,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc')
);

CREATE UNIQUE INDEX ux_conversion_rates_provider_source_target_active
    ON currency.conversion_rates (provider_id, source_code, target_code)
    WHERE active = TRUE;

CREATE TABLE currency.shedlock
(
    name       VARCHAR(64)  NOT NULL PRIMARY KEY,
    lock_until timestamptz  NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    locked_at  timestamptz  NOT NULL DEFAULT (NOW() AT TIME ZONE 'utc'),
    locked_by  VARCHAR(255) NOT NULL
);



