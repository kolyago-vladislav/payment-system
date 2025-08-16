CREATE SCHEMA IF NOT EXISTS payment_provider;
CREATE SCHEMA IF NOT EXISTS payment_provider_history;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
SET search_path TO payment_provider,payment_provider_history,public;