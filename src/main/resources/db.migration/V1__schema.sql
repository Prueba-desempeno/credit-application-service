-- ================================
-- V1__schema.sql
-- Creación de tablas base
-- ================================

Affiliate.java
│ │ │ │ │ ├── CreditApplication.java
│ │ │ │ │ └── RiskEvaluation.java


-- ==== TABLA ROLES ====
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- ==== TABLA USERS ====
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- ==== TABLA USER_ROLES (Many-to-Many) ====
CREATE TABLE user_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL
);

-- ==== ENUM SIMULADOS (CHECKS) ====
-- Estado del afiliado: ACTIVO / INACTIVO
-- Estado de una solicitud: PENDIENTE / APROBADO / RECHAZADO
-- Nivel riesgo: ALTO / MEDIO / BAJO

-- ==== TABLA AFFILIATES ====
CREATE TABLE affiliates (
    id SERIAL PRIMARY KEY,
    document VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    salary NUMERIC(15,2) NOT NULL CHECK (salary > 0),
    affiliation_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVO', 'INACTIVO'))
);

-- ==== TABLA CREDIT_APPLICATIONS ====
CREATE TABLE credit_applications (
    id SERIAL PRIMARY KEY,
    affiliate_id INTEGER NOT NULL,
    amount NUMERIC(15,2) NOT NULL CHECK (amount > 0),
    term_months INTEGER NOT NULL CHECK (term_months > 0),
    interest_rate NUMERIC(5,2) NOT NULL CHECK (interest_rate > 0),
    request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDIENTE', 'APROBADO', 'RECHAZADO'))
);

-- ==== TABLA RISK_EVALUATIONS ====
CREATE TABLE risk_evaluations (
    id SERIAL PRIMARY KEY,
    credit_application_id INTEGER NOT NULL UNIQUE,
    score INTEGER NOT NULL CHECK (score BETWEEN 300 AND 950),
    risk_level VARCHAR(20) NOT NULL CHECK (risk_level IN ('ALTO', 'MEDIO', 'BAJO')),
    detail TEXT
);

-- ==== ÍNDICES OPCIONALES ====
CREATE INDEX idx_affiliate_document ON affiliates(document);
CREATE INDEX idx_credit_application_status ON credit_applications(status);
CREATE INDEX idx_user_username ON users(username);
