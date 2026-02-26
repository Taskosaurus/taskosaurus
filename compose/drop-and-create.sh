#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Load credentials from .env
set -a
source "$SCRIPT_DIR/.env"
set +a

export PGPASSWORD="$POSTGRES_PASSWORD"

PSQL="psql -h localhost -p ${POSTGRES_PORT:-5432} -U $POSTGRES_USER -d $POSTGRES_DB"

echo "==> Running setup.sql (drop + create) ..."
$PSQL -f "$SCRIPT_DIR/setup.sql"

echo "==> Running insert.sql (seed data) ..."
$PSQL -f "$SCRIPT_DIR/insert.sql"

echo "==> Done."
