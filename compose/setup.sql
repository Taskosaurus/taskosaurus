-- =============================================================
-- Taskosaurus — Full DDL
-- PostgreSQL 16
-- Drop everything and recreate from scratch.
-- =============================================================

-- -------------------------------------------------------
-- DROP (reverse dependency order)
-- -------------------------------------------------------
DROP TABLE IF EXISTS vote                      CASCADE;
DROP TABLE IF EXISTS group_membership_snapshot CASCADE;
DROP TABLE IF EXISTS group_question            CASCADE;
DROP TABLE IF EXISTS question                  CASCADE;
DROP TABLE IF EXISTS group_membership          CASCADE;
DROP TABLE IF EXISTS groups                    CASCADE;
DROP TABLE IF EXISTS player                    CASCADE;

-- -------------------------------------------------------
-- player
-- -------------------------------------------------------
CREATE TABLE player (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    nickname    TEXT        NOT NULL UNIQUE,
    pin_hash    TEXT        NOT NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

-- -------------------------------------------------------
-- groups  (avoid reserved word "group")
-- -------------------------------------------------------
CREATE TABLE groups (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    name        TEXT        NOT NULL,
    owner_id    UUID        NOT NULL REFERENCES player(id),
    join_link   TEXT        NOT NULL UNIQUE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at  TIMESTAMPTZ
);

-- -------------------------------------------------------
-- group_membership
-- -------------------------------------------------------
CREATE TABLE group_membership (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id    UUID        NOT NULL REFERENCES groups(id),
    player_id   UUID        NOT NULL REFERENCES player(id),
    joined_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    left_at     TIMESTAMPTZ
);

-- -------------------------------------------------------
-- question
-- -------------------------------------------------------
CREATE TABLE question (
    id      UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    text    TEXT    NOT NULL
);

-- -------------------------------------------------------
-- group_question
-- One row per group per day. closes_at = created_at + 24h.
-- -------------------------------------------------------
CREATE TABLE group_question (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    group_id    UUID        NOT NULL REFERENCES groups(id),
    question_id UUID        NOT NULL REFERENCES question(id),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    closes_at   TIMESTAMPTZ NOT NULL,
    -- Invariant I1: max 1 open question per group at a time
    CONSTRAINT uq_group_question_open UNIQUE (group_id, created_at)
);

-- -------------------------------------------------------
-- group_membership_snapshot
-- Frozen copy of group members at question-creation time.
-- -------------------------------------------------------
CREATE TABLE group_membership_snapshot (
    id                UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    group_question_id UUID    NOT NULL REFERENCES group_question(id),
    player_id         UUID    NOT NULL REFERENCES player(id),
    membership_id     UUID    NOT NULL REFERENCES group_membership(id),
    display_name      TEXT    NOT NULL
);

-- -------------------------------------------------------
-- vote
-- -------------------------------------------------------
CREATE TABLE vote (
    id                        UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    group_question_id         UUID        NOT NULL REFERENCES group_question(id),
    answering_membership_id   UUID        NOT NULL REFERENCES group_membership(id),
    target_snapshot_player_id UUID        NOT NULL REFERENCES group_membership_snapshot(id),
    created_at                TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    -- Invariant I2: max 1 vote per membership per question
    CONSTRAINT uq_vote_per_membership UNIQUE (answering_membership_id, group_question_id)
);
