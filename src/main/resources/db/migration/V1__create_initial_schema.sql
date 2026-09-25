-- ============================================================
-- ISera Biological - IT Asset Management System
-- Migration: V1
-- Description: Initial database schema
-- ============================================================


-- ============================================================
-- 1. ROLES
-- ============================================================

CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       name VARCHAR(50) NOT NULL,
                       description VARCHAR(255),

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       CONSTRAINT uk_roles_name UNIQUE (name)
);


-- ============================================================
-- 2. USERS
-- ============================================================

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       username VARCHAR(50) NOT NULL,
                       email VARCHAR(150) NOT NULL,
                       password VARCHAR(255) NOT NULL,

                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100),

                       is_active BOOLEAN NOT NULL DEFAULT TRUE,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       CONSTRAINT uk_users_username UNIQUE (username),
                       CONSTRAINT uk_users_email UNIQUE (email)
);


-- ============================================================
-- 3. USER ROLES
-- ============================================================

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,

                            PRIMARY KEY (user_id, role_id),

                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id)
                                    REFERENCES roles(id)
                                    ON DELETE CASCADE
);


-- ============================================================
-- 4. DEPARTMENTS
-- ============================================================

CREATE TABLE departments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,

                             name VARCHAR(100) NOT NULL,
                             code VARCHAR(30) NOT NULL,
                             description VARCHAR(255),

                             is_active BOOLEAN NOT NULL DEFAULT TRUE,

                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                 ON UPDATE CURRENT_TIMESTAMP,

                             CONSTRAINT uk_departments_name UNIQUE (name),
                             CONSTRAINT uk_departments_code UNIQUE (code)
);


-- ============================================================
-- 5. LOCATIONS
-- ============================================================

CREATE TABLE locations (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,

                           name VARCHAR(100) NOT NULL,
                           code VARCHAR(30) NOT NULL,

                           address_line1 VARCHAR(255),
                           address_line2 VARCHAR(255),

                           city VARCHAR(100) NOT NULL,
                           state VARCHAR(100),
                           postal_code VARCHAR(20),
                           country VARCHAR(100) NOT NULL,

                           is_active BOOLEAN NOT NULL DEFAULT TRUE,

                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP,

                           CONSTRAINT uk_locations_code UNIQUE (code)
);


-- ============================================================
-- 6. EMPLOYEES
-- ============================================================

CREATE TABLE employees (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,

                           employee_code VARCHAR(50) NOT NULL,

                           first_name VARCHAR(100) NOT NULL,
                           last_name VARCHAR(100),

                           email VARCHAR(150) NOT NULL,
                           phone VARCHAR(30),

                           department_id BIGINT NOT NULL,
                           location_id BIGINT NOT NULL,

                           designation VARCHAR(100),

                           joining_date DATE,

                           status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP,

                           CONSTRAINT uk_employees_code
                               UNIQUE (employee_code),

                           CONSTRAINT uk_employees_email
                               UNIQUE (email),

                           CONSTRAINT fk_employees_department
                               FOREIGN KEY (department_id)
                                   REFERENCES departments(id),

                           CONSTRAINT fk_employees_location
                               FOREIGN KEY (location_id)
                                   REFERENCES locations(id),

                           CONSTRAINT chk_employees_status
                               CHECK (
                                   status IN (
                                              'ACTIVE',
                                              'INACTIVE',
                                              'EXITED'
                                       )
                                   )
);


-- ============================================================
-- 7. ASSET CATEGORIES
-- ============================================================

CREATE TABLE asset_categories (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                  name VARCHAR(100) NOT NULL,
                                  code VARCHAR(30) NOT NULL,
                                  description VARCHAR(255),

                                  is_active BOOLEAN NOT NULL DEFAULT TRUE,

                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                      ON UPDATE CURRENT_TIMESTAMP,

                                  CONSTRAINT uk_asset_categories_name
                                      UNIQUE (name),

                                  CONSTRAINT uk_asset_categories_code
                                      UNIQUE (code)
);


-- ============================================================
-- 8. VENDORS
-- ============================================================

CREATE TABLE vendors (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,

                         name VARCHAR(150) NOT NULL,
                         code VARCHAR(50) NOT NULL,

                         contact_person VARCHAR(150),
                         email VARCHAR(150),
                         phone VARCHAR(30),

                         address VARCHAR(500),

                         is_active BOOLEAN NOT NULL DEFAULT TRUE,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP,

                         CONSTRAINT uk_vendors_code
                             UNIQUE (code)
);


-- ============================================================
-- 9. ASSETS
-- ============================================================

CREATE TABLE assets (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,

                        asset_tag VARCHAR(50) NOT NULL,
                        serial_number VARCHAR(100),

                        category_id BIGINT NOT NULL,
                        vendor_id BIGINT,

                        brand VARCHAR(100) NOT NULL,
                        model VARCHAR(150),

                        purchase_date DATE,
                        purchase_cost DECIMAL(12,2),

                        warranty_expiry_date DATE,

                        location_id BIGINT NOT NULL,

                        status VARCHAR(30) NOT NULL DEFAULT 'IN_STOCK',

                        asset_condition VARCHAR(30) NOT NULL DEFAULT 'GOOD',

                        description VARCHAR(500),

                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

                        CONSTRAINT uk_assets_asset_tag
                            UNIQUE (asset_tag),

                        CONSTRAINT uk_assets_serial_number
                            UNIQUE (serial_number),

                        CONSTRAINT fk_assets_category
                            FOREIGN KEY (category_id)
                                REFERENCES asset_categories(id),

                        CONSTRAINT fk_assets_vendor
                            FOREIGN KEY (vendor_id)
                                REFERENCES vendors(id),

                        CONSTRAINT fk_assets_location
                            FOREIGN KEY (location_id)
                                REFERENCES locations(id),

                        CONSTRAINT chk_assets_status
                            CHECK (
                                status IN (
                                           'IN_STOCK',
                                           'ASSIGNED',
                                           'UNDER_MAINTENANCE',
                                           'RETURNED',
                                           'RETIRED',
                                           'DISPOSED'
                                    )
                                ),

                        CONSTRAINT chk_assets_condition
                            CHECK (
                                asset_condition IN (
                                                    'NEW',
                                                    'GOOD',
                                                    'FAIR',
                                                    'DAMAGED'
                                    )
                                ),

                        CONSTRAINT chk_assets_purchase_cost
                            CHECK (
                                purchase_cost IS NULL
                                    OR purchase_cost >= 0
                                ),

                        CONSTRAINT chk_assets_warranty
                            CHECK (
                                warranty_expiry_date IS NULL
                                    OR purchase_date IS NULL
                                    OR warranty_expiry_date >= purchase_date
                                )
);


-- ============================================================
-- 10. ASSET ASSIGNMENTS
-- ============================================================

CREATE TABLE asset_assignments (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                   asset_id BIGINT NOT NULL,
                                   employee_id BIGINT NOT NULL,

                                   assigned_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                   returned_date DATETIME,

                                   assigned_by BIGINT NOT NULL,
                                   returned_by BIGINT,

                                   status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

                                   remarks VARCHAR(500),

                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                       ON UPDATE CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_assignment_asset
                                       FOREIGN KEY (asset_id)
                                           REFERENCES assets(id),

                                   CONSTRAINT fk_assignment_employee
                                       FOREIGN KEY (employee_id)
                                           REFERENCES employees(id),

                                   CONSTRAINT fk_assignment_assigned_by
                                       FOREIGN KEY (assigned_by)
                                           REFERENCES users(id),

                                   CONSTRAINT fk_assignment_returned_by
                                       FOREIGN KEY (returned_by)
                                           REFERENCES users(id),

                                   CONSTRAINT chk_assignment_status
                                       CHECK (
                                           status IN (
                                                      'ACTIVE',
                                                      'RETURNED'
                                               )
                                           ),

                                   CONSTRAINT chk_assignment_dates
                                       CHECK (
                                           returned_date IS NULL
                                               OR returned_date >= assigned_date
                                           )
);


-- ============================================================
-- 11. MAINTENANCE RECORDS
-- ============================================================

CREATE TABLE maintenance_records (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                     asset_id BIGINT NOT NULL,

                                     reported_by BIGINT,

                                     assigned_technician BIGINT,

                                     issue_description VARCHAR(1000) NOT NULL,

                                     reported_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     started_date DATETIME,
                                     resolved_date DATETIME,

                                     status VARCHAR(30) NOT NULL DEFAULT 'OPEN',

                                     resolution VARCHAR(1000),

                                     cost DECIMAL(12,2),

                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                         ON UPDATE CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_maintenance_asset
                                         FOREIGN KEY (asset_id)
                                             REFERENCES assets(id),

                                     CONSTRAINT fk_maintenance_reported_by
                                         FOREIGN KEY (reported_by)
                                             REFERENCES employees(id),

                                     CONSTRAINT fk_maintenance_technician
                                         FOREIGN KEY (assigned_technician)
                                             REFERENCES users(id),

                                     CONSTRAINT chk_maintenance_status
                                         CHECK (
                                             status IN (
                                                        'OPEN',
                                                        'IN_PROGRESS',
                                                        'RESOLVED',
                                                        'CLOSED',
                                                        'CANCELLED'
                                                 )
                                             ),

                                     CONSTRAINT chk_maintenance_cost
                                         CHECK (
                                             cost IS NULL
                                                 OR cost >= 0
                                             ),

                                     CONSTRAINT chk_maintenance_dates
                                         CHECK (
                                             resolved_date IS NULL
                                                 OR resolved_date >= reported_date
                                             )
);


-- ============================================================
-- 12. AUDIT LOGS
-- ============================================================

CREATE TABLE audit_logs (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,

                            user_id BIGINT,

                            action VARCHAR(100) NOT NULL,

                            entity_type VARCHAR(100) NOT NULL,
                            entity_id BIGINT,

                            old_value JSON,
                            new_value JSON,

                            ip_address VARCHAR(45),

                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_audit_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
);


-- ============================================================
-- 13. REFRESH TOKENS
-- ============================================================

CREATE TABLE refresh_tokens (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                token_hash VARCHAR(255) NOT NULL,
                                expiry_date DATETIME NOT NULL,
                                revoked BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT uk_refresh_token_hash
                                    UNIQUE (token_hash),
                                CONSTRAINT fk_refresh_token_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
                                        ON DELETE CASCADE
);


-- ============================================================
-- INDEXES
-- ============================================================

CREATE INDEX idx_employees_department
    ON employees(department_id);

CREATE INDEX idx_employees_location
    ON employees(location_id);

CREATE INDEX idx_employees_status
    ON employees(status);


CREATE INDEX idx_assets_category
    ON assets(category_id);

CREATE INDEX idx_assets_location
    ON assets(location_id);

CREATE INDEX idx_assets_vendor
    ON assets(vendor_id);

CREATE INDEX idx_assets_status
    ON assets(status);

CREATE INDEX idx_assets_warranty_expiry
    ON assets(warranty_expiry_date);


CREATE INDEX idx_assignment_asset
    ON asset_assignments(asset_id);

CREATE INDEX idx_assignment_employee
    ON asset_assignments(employee_id);

CREATE INDEX idx_assignment_asset_status
    ON asset_assignments(asset_id, status);


CREATE INDEX idx_maintenance_asset
    ON maintenance_records(asset_id);

CREATE INDEX idx_maintenance_status
    ON maintenance_records(status);


CREATE INDEX idx_audit_entity
    ON audit_logs(entity_type, entity_id);

CREATE INDEX idx_audit_user
    ON audit_logs(user_id);

CREATE INDEX idx_audit_created_at
    ON audit_logs(created_at);


CREATE INDEX idx_refresh_token_user
    ON refresh_tokens(user_id);

CREATE INDEX idx_refresh_token_expiry
    ON refresh_tokens(expiry_date);