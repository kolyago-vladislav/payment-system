#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
SELECT 'CREATE DATABASE transaction_shard_1'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'transaction_shard_1')\gexec
EOSQL

echo -e "wal_level = 'logical'" >> $PGDATA/postgresql.conf