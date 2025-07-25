INSERT
INTO
    "transaction".wallet_types
(
    "id",
    "name",
    currency_code,
    status
)
VALUES('123e4567-e89b-12d3-a456-426614174001'::uuid, 'wallet_type_name', 'BLR', 'ACTIVE');

INSERT
INTO
    "transaction".wallets
(
    "id",
    "name",
    wallet_type_id,
    user_id,
    status,
    balance
)
VALUES('123e4567-e89b-12d3-a456-426614174002'::uuid, 'wallet_name', '123e4567-e89b-12d3-a456-426614174001'::uuid, '123e4567-e89b-12d3-a456-426614174000'::uuid, 'ACTIVE', 0.0);

INSERT
INTO
    "transaction".wallets
(
    "id",
    "name",
    wallet_type_id,
    user_id,
    status,
    balance
)
VALUES('123e4567-e89b-12d3-a456-426614174003'::uuid, 'wallet_name', '123e4567-e89b-12d3-a456-426614174001'::uuid, '123e4567-e89b-12d3-a456-426614174000'::uuid, 'ACTIVE', 0.0);
