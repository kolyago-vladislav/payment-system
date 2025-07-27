CREATE SCHEMA IF NOT EXISTS transaction;
CREATE SCHEMA IF NOT EXISTS transaction_history;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
SET search_path TO transaction,transaction_history,public;