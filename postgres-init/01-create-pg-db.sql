-- Runs the first time the postgres container starts.
-- The default DB (auth_db) is created via POSTGRES_DB env var.
-- This script creates the second DB (pg_db).
CREATE DATABASE pg_db;