-- =====================================================================
-- V2_0 | Bounded Context: INVENTORY (core)
-- user_id referencia identity.user SIN FK (integridad a nivel de aplicación)
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS inventory;

CREATE TABLE inventory.company (
    id             UUID         PRIMARY KEY,
    user_id        UUID         NOT NULL,
    name           VARCHAR(255) NOT NULL,
    tax_id         VARCHAR(50),
    fiscal_address VARCHAR(500),
    phone          VARCHAR(50),
    created_at     TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_company_user_id ON inventory.company (user_id);

CREATE TABLE inventory.supplier (
    id                  UUID         PRIMARY KEY,
    company_id          UUID         NOT NULL,
    name                VARCHAR(255) NOT NULL,
    contact             VARCHAR(255),
    delivery_time_days  INT,
    CONSTRAINT fk_supplier_company FOREIGN KEY (company_id) REFERENCES inventory.company (id),
    CONSTRAINT ck_supplier_delivery CHECK (delivery_time_days IS NULL OR delivery_time_days >= 0)
);
CREATE INDEX idx_supplier_company_id ON inventory.supplier (company_id);

CREATE TABLE inventory.category (
    id    UUID         PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE inventory.subcategory (
    id           UUID         PRIMARY KEY,
    category_id  UUID         NOT NULL,
    name         VARCHAR(255) NOT NULL,
    CONSTRAINT fk_subcategory_category FOREIGN KEY (category_id) REFERENCES inventory.category (id),
    CONSTRAINT uq_subcategory_name UNIQUE (category_id, name)
);

CREATE TABLE inventory.product (
    id              UUID         PRIMARY KEY,
    company_id      UUID         NOT NULL,
    supplier_id     UUID,
    subcategory_id  UUID         NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    stock           INT          NOT NULL DEFAULT 0,
    fair_eligible   BOOLEAN      NOT NULL DEFAULT false,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT fk_product_company     FOREIGN KEY (company_id)     REFERENCES inventory.company (id),
    CONSTRAINT fk_product_supplier    FOREIGN KEY (supplier_id)    REFERENCES inventory.supplier (id),
    CONSTRAINT fk_product_subcategory FOREIGN KEY (subcategory_id) REFERENCES inventory.subcategory (id),
    CONSTRAINT ck_product_stock CHECK (stock >= 0)
);
CREATE INDEX idx_product_company_id    ON inventory.product (company_id);
CREATE INDEX idx_product_fair_eligible ON inventory.product (fair_eligible);
CREATE INDEX idx_product_supplier_id   ON inventory.product (supplier_id);
CREATE INDEX idx_product_subcategory   ON inventory.product (subcategory_id);
-- Alerta de stock bajo del dashboard: consulta por empresa + stock
CREATE INDEX idx_product_company_stock ON inventory.product (company_id, stock);
