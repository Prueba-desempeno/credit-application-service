-- ================================
-- V2__relaciones.sql
-- Relaciones entre tablas
-- ================================

-- ==== USER_ROLES FK ====
ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;

-- ==== CREDIT_APPLICATION → AFFILIATE (1–N) ====
ALTER TABLE credit_applications
    ADD CONSTRAINT fk_credit_application_affiliate
        FOREIGN KEY (affiliate_id) REFERENCES affiliates(id) ON DELETE CASCADE;

-- ==== RISK_EVALUATION → CREDIT_APPLICATION (1–1) ====
ALTER TABLE risk_evaluations
    ADD CONSTRAINT fk_risk_evaluation_credit_application
        FOREIGN KEY (credit_application_id) REFERENCES credit_applications(id) ON DELETE CASCADE;
