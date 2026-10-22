-- =====================================================================
-- V5_0 | Bounded Context: FAIR
-- company/product son copias locales sincronizadas por eventos de Inventory
-- (solo productos con fair_eligible = true). fair_product.company_id sin FK.
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS fair;

CREATE TABLE fair.fair (
    id                     UUID         PRIMARY KEY,
    name                   VARCHAR(255) NOT NULL,
    start_date             TIMESTAMP    NOT NULL,
    end_date               TIMESTAMP    NOT NULL,
    registration_deadline  TIMESTAMP    NOT NULL,
    status                 VARCHAR(20)  NOT NULL DEFAULT 'open',
    CONSTRAINT ck_fair_status CHECK (status IN ('open', 'closed', 'finished')),
    CONSTRAINT ck_fair_dates  CHECK (end_date > start_date)
);
CREATE INDEX idx_fair_status_dates ON fair.fair (status, start_date, end_date);

CREATE TABLE fair.company (
    id        UUID         PRIMARY KEY,   -- mismo UUID que inventory.company
    name      VARCHAR(255) NOT NULL,
    logo_url  VARCHAR(1000),
    contact   VARCHAR(255)
);

CREATE TABLE fair.product (
    id           UUID         PRIMARY KEY,   -- mismo UUID que inventory.product
    company_id   UUID         NOT NULL,
    name         VARCHAR(255) NOT NULL,
    description  TEXT,
    image_url    VARCHAR(1000),
    CONSTRAINT fk_fair_product_company FOREIGN KEY (company_id) REFERENCES fair.company (id)
);
CREATE INDEX idx_fair_product_company_id ON fair.product (company_id);

CREATE TABLE fair.fair_company (
    id             UUID         PRIMARY KEY,
    fair_id        UUID         NOT NULL,
    company_id     UUID         NOT NULL,
    registered_at  TIMESTAMP    NOT NULL DEFAULT now(),
    status         VARCHAR(20)  NOT NULL DEFAULT 'invited',
    CONSTRAINT fk_fair_company_fair    FOREIGN KEY (fair_id)    REFERENCES fair.fair (id),
    CONSTRAINT fk_fair_company_company FOREIGN KEY (company_id) REFERENCES fair.company (id),
    CONSTRAINT uq_fair_company         UNIQUE (fair_id, company_id),
    CONSTRAINT ck_fair_company_status  CHECK (status IN ('invited', 'confirmed', 'cancelled'))
);
CREATE INDEX idx_fair_company_company_id ON fair.fair_company (company_id);

CREATE TABLE fair.fair_product (
    id             UUID          PRIMARY KEY,
    fair_id        UUID          NOT NULL,
    company_id     UUID          NOT NULL,   -- desnormalizado, sin FK
    product_id     UUID          NOT NULL,
    fair_price     DECIMAL(10,2),
    registered_at  TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT fk_fair_product_fair    FOREIGN KEY (fair_id)    REFERENCES fair.fair (id),
    CONSTRAINT fk_fair_product_product FOREIGN KEY (product_id) REFERENCES fair.product (id),
    CONSTRAINT uq_fair_product         UNIQUE (fair_id, product_id),
    CONSTRAINT ck_fair_product_price   CHECK (fair_price IS NULL OR fair_price >= 0)
);
CREATE INDEX idx_fair_product_fair_id         ON fair.fair_product (fair_id);
CREATE INDEX idx_fair_product_fair_company    ON fair.fair_product (fair_id, company_id);
CREATE INDEX idx_fair_product_product_id      ON fair.fair_product (product_id);
