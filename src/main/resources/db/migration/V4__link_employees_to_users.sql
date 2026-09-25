-- ============================================================
-- ISera Biological - IT Asset Management System
-- Migration: V4
-- Description: Link employees to application users
-- ============================================================

ALTER TABLE employees
    ADD COLUMN user_id BIGINT NULL;

ALTER TABLE employees
    ADD CONSTRAINT uk_employees_user
        UNIQUE (user_id);

ALTER TABLE employees
    ADD CONSTRAINT fk_employees_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE SET NULL;