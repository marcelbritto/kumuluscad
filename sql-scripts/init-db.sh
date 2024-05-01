#!/bin/bash

# Espera até que o PostgreSQL esteja pronto para aceitar conexões
until pg_isready -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER"; do
  echo "Aguardando o PostgreSQL iniciar..."
  sleep 1
done

# Executa os scripts SQL
psql -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /path/to/sql-scripts/01-create_tables.sql
psql -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /path/to/sql-scripts/02-insert_values.sql

echo "Scripts SQL executados com sucesso."