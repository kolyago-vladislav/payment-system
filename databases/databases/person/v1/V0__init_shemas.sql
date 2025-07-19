CREATE SCHEMA if NOT EXISTS person;
CREATE SCHEMA if NOT EXISTS person_history;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
SET search_path TO person,person_history,public;