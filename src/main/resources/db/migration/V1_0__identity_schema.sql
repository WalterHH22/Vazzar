-- =====================================================================
-- V1_0 | Bounded Context: IDENTITY
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS identity;

CREATE TABLE identity."user" (
    id             UUID         PRIMARY KEY,
    email          VARCHAR(255) NOT NULL,
    password_hash  VARCHAR(255) NOT NULL,
    role           VARCHAR(20)  NOT NULL,
    status         VARCHAR(20)  NOT NULL DEFAULT 'active',
    created_at     TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT uq_user_email  UNIQUE (email),
    CONSTRAINT ck_user_role   CHECK (role   IN ('admin', 'seller', 'buyer')),
    CONSTRAINT ck_user_status CHECK (status IN ('active', 'inactive'))
);

-- Login insensible a mayúsculas (evita duplicados tipo Foo@x.com / foo@x.com)
CREATE UNIQUE INDEX uq_user_email_lower ON identity."user" (lower(email));
CREATE INDEX idx_user_role_status        ON identity."user" (role, status);
