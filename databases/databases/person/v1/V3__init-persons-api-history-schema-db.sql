CREATE TABLE person_history.revinfo
(
    rev      bigserial NOT NULL PRIMARY KEY,
    revtstmp bigint
);

CREATE TABLE person_history.countries_history
(
    id            serial PRIMARY KEY,
    revision      bigint       NOT NULL,
    revision_type SMALLINT     NOT NULL,
    active        boolean      NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    name          VARCHAR(128) NOT NULL,
    code          VARCHAR(3)   NOT NULL
);

CREATE TABLE person_history.addresses_history
(
    id            uuid,
    revision      bigint       NOT NULL,
    revision_type SMALLINT     NOT NULL,
    active        boolean      NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    country_id    INTEGER      NOT NULL,
    address       VARCHAR(128) NOT NULL,
    zip_code      VARCHAR(32)  NOT NULL,
    city          VARCHAR(32)  NOT NULL
);

CREATE TABLE person_history.users_history
(
    id            uuid,
    revision      bigint        NOT NULL,
    revision_type SMALLINT      NOT NULL,
    active        boolean       NOT NULL DEFAULT TRUE,
    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    email         VARCHAR(1024) NOT NULL,
    first_name    VARCHAR(32)   NOT NULL,
    last_name     VARCHAR(32)   NOT NULL,
    address_id    uuid          NOT NULL
);

CREATE TABLE person_history.individuals_history
(
    id              uuid,
    revision        bigint      NOT NULL,
    revision_type   SMALLINT    NOT NULL,
    active          boolean     NOT NULL DEFAULT TRUE,
    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated         TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    passport_number VARCHAR(32) NOT NULL,
    phone_number    VARCHAR(32) NOT NULL,
    user_id         uuid        NOT NULL
);