#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
SELECT 'CREATE DATABASE person'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'person')\gexec
EOSQL

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
SELECT 'CREATE DATABASE transaction'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'transaction')\gexec
EOSQL