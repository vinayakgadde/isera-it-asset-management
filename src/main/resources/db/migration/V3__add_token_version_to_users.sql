-- ============================================================
-- ISera Biological - IT Asset Management System
-- Migration: V3
-- Description: Add token version for JWT invalidation
-- ============================================================

ALTER TABLE users
    ADD COLUMN token_version BIGINT NOT NULL DEFAULT 0;