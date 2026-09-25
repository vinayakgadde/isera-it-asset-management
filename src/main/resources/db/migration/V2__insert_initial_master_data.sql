-- ============================================================
-- ISera Biological - IT Asset Management System
-- Migration: V2
-- Description: Insert initial master data
-- ============================================================


-- ============================================================
-- 1. ROLES
-- ============================================================

INSERT INTO roles (
    name,
    description
)
VALUES
    (
        'ADMIN',
        'Full system access'
    ),
    (
        'IT_ADMIN',
        'IT asset administration access'
    ),
    (
        'IT_SUPPORT',
        'IT support operations'
    ),
    (
        'MANAGER',
        'Department level access'
    ),
    (
        'EMPLOYEE',
        'Employee self-service access'
    );


-- ============================================================
-- 2. DEPARTMENTS
-- ============================================================

INSERT INTO departments (
    name,
    code,
    description
)
VALUES
    (
        'Information Technology',
        'IT',
        'Information Technology Department'
    ),
    (
        'Human Resources',
        'HR',
        'Human Resources Department'
    ),
    (
        'Finance',
        'FIN',
        'Finance Department'
    ),
    (
        'Sales',
        'SALES',
        'Sales Department'
    ),
    (
        'Operations',
        'OPS',
        'Operations Department'
    );

-- ============================================================
-- 3. LOCATIONS
-- ============================================================

INSERT INTO locations (
    name,
    code,
    city,
    state,
    country
)
VALUES
    (
        'Pune Office',
        'PUNE',
        'Pune',
        'Maharashtra',
        'India'
    ),
    (
        'Mumbai Office',
        'MUM',
        'Mumbai',
        'Maharashtra',
        'India'
    );


-- ============================================================
-- 4. ASSET CATEGORIES
-- ============================================================

INSERT INTO asset_categories (
    name,
    code,
    description
)
VALUES
    (
        'Laptop',
        'LAP',
        'Laptop computers'
    ),
    (
        'Desktop',
        'DESK',
        'Desktop computers'
    ),
    (
        'Monitor',
        'MON',
        'Computer monitors'
    ),
    (
        'Mobile Phone',
        'MOB',
        'Company mobile phones'
    ),
    (
        'Printer',
        'PRINT',
        'Printers and multifunction devices'
    ),
    (
        'Network Device',
        'NET',
        'Routers, switches and network devices'
    ),
    (
        'Keyboard',
        'KEY',
        'Computer keyboards'
    ),
    (
        'Mouse',
        'MOUSE',
        'Computer mouse devices'
    );

