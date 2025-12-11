-- ================================
-- V3__data_inicial.sql
-- Datos de inicio
-- ================================

-- Roles del sistema
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_ANALISTA'),
('ROLE_AFILIADO');

-- Usuario administrador
-- NOTA: El password debe reemplazarse por el hash REAL generado por Spring.
INSERT INTO users (username, password, enabled)
VALUES ('admin', '$2a$10$placeholderHashAdmin123456789012345678901234567890', TRUE);

-- Asignar rol admin
INSERT INTO user_roles (user_id, role_id)
VALUES (
    (SELECT id FROM users WHERE username = 'admin'),
    (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')
);

-- Afiliados de prueba
INSERT INTO affiliates (document, name, salary, affiliation_date, status)
VALUES
    ('1010101010', 'Juan Pérez', 2500000, '2022-01-15', 'ACTIVO'),
    ('2020202020', 'Ana Gómez', 3500000, '2021-06-20', 'ACTIVO'),
    ('3030303030', 'Carlos Ruiz', 1200000, '2023-02-10', 'INACTIVO');
