CREATE SCHEMA IF NOT EXISTS currency;
CREATE SCHEMA IF NOT EXISTS currency_history;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
SET search_path TO currency,currency_history,public;