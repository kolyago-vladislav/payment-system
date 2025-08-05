INSERT INTO transaction.wallet_types ("id", name, currency_code, status)
VALUES
    ('00000000-1111-2222-3333-000000000001'::uuid, 'Main Wallet',       'USD', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000002'::uuid, 'Savings Wallet',    'USD', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000003'::uuid, 'Business Wallet',   'USD', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000004'::uuid, 'Crypto Wallet',     'BTC', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000005'::uuid, 'Investment Wallet', 'EUR', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000006'::uuid, 'Travel Wallet',     'USD', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000007'::uuid, 'Charity Wallet',    'USD', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000008'::uuid, 'Payroll Wallet',    'USD', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000009'::uuid, 'Gift Wallet',       'USD', 'ACTIVE'),
    ('00000000-1111-2222-3333-000000000010'::uuid, 'Backup Wallet',     'USD', 'ACTIVE');