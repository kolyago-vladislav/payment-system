INSERT
INTO
    currency.currencies (code, iso_code, description, symbol)
VALUES
    ('EUR', 978, 'Euro', '€'),
    ('AUD', 36, 'Australian Dollar', '$'),
    ('BGN', 975, 'Bulgarian Lev', 'лв'),
    ('BRL', 986, 'Brazilian Real', 'R$'),
    ('CAD', 124, 'Canadian Dollar', '$'),
    ('CHF', 756, 'Swiss Franc', 'Fr'),
    ('CNY', 156, 'Chinese Yuan', '¥'),
    ('CZK', 203, 'Czech Koruna', 'Kč'),
    ('DKK', 208, 'Danish Krone', 'kr'),
    ('GBP', 826, 'British Pound', '£'),
    ('HKD', 344, 'Hong Kong Dollar', '$'),
    ('HUF', 348, 'Hungarian Forint', 'Ft'),
    ('IDR', 360, 'Indonesian Rupiah', 'Rp'),
    ('ILS', 376, 'Israeli New Shekel', '₪'),
    ('INR', 356, 'Indian Rupee', '₹'),
    ('ISK', 352, 'Icelandic Króna', 'kr'),
    ('JPY', 392, 'Japanese Yen', '¥'),
    ('KRW', 410, 'South Korean Won', '₩'),
    ('MXN', 484, 'Mexican Peso', '$'),
    ('MYR', 458, 'Malaysian Ringgit', 'RM'),
    ('NOK', 578, 'Norwegian Krone', 'kr'),
    ('NZD', 554, 'New Zealand Dollar', '$'),
    ('PHP', 608, 'Philippine Peso', '₱'),
    ('PLN', 985, 'Polish Zloty', 'zł'),
    ('RON', 946, 'Romanian Leu', 'lei'),
    ('SEK', 752, 'Swedish Krona', 'kr'),
    ('SGD', 702, 'Singapore Dollar', '$'),
    ('THB', 764, 'Thai Baht', '฿'),
    ('TRY', 949, 'Turkish Lira', '₺'),
    ('USD', 840, 'US Dollar', '$'),
    ('ZAR', 710, 'South African Rand', 'R');

INSERT
INTO
    currency.rate_providers (id, name, description, priority)
VALUES
    ('FRANKFURTER',
     'Frankfurter',
     'Free and open-source currency rates API by the European Central Bank (https://frankfurter.dev/)',
     1);