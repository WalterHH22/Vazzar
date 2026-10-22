-- =====================================================================
-- V3_0 | Bounded Context: BILLING
-- user_id, order_id y product_id son referencias SIN FK (otros contextos)
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS billing;

CREATE TABLE billing.billing_customer (
    id       UUID         PRIMARY KEY,
    user_id  UUID         NOT NULL,
    name     VARCHAR(255) NOT NULL,
    tax_id   VARCHAR(50)              -- NIT o DUI
);
CREATE INDEX idx_billing_customer_user_id ON billing.billing_customer (user_id);

CREATE TABLE billing.invoice (
    id                   UUID          PRIMARY KEY,
    order_id             UUID          NOT NULL,
    source_channel       VARCHAR(20)   NOT NULL,
    billing_customer_id  UUID          NOT NULL,
    total                DECIMAL(10,2) NOT NULL,
    status               VARCHAR(20)   NOT NULL DEFAULT 'issued',
    issued_at            TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT fk_invoice_customer FOREIGN KEY (billing_customer_id) REFERENCES billing.billing_customer (id),
    CONSTRAINT ck_invoice_channel  CHECK (source_channel IN ('ecommerce', 'fair')),
    CONSTRAINT ck_invoice_status   CHECK (status IN ('issued', 'cancelled')),
    CONSTRAINT ck_invoice_total    CHECK (total >= 0)
);
CREATE INDEX idx_invoice_billing_customer ON billing.invoice (billing_customer_id);
CREATE INDEX idx_invoice_order            ON billing.invoice (source_channel, order_id);
CREATE INDEX idx_invoice_issued_at        ON billing.invoice (issued_at DESC);

CREATE TABLE billing.invoice_item (
    id            UUID          PRIMARY KEY,
    invoice_id    UUID          NOT NULL,
    product_id    UUID          NOT NULL,   -- copia, sin FK
    product_name  VARCHAR(255)  NOT NULL,   -- desnormalizado: nombre histórico
    quantity      INT           NOT NULL,
    unit_price    DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_invoice_item_invoice FOREIGN KEY (invoice_id) REFERENCES billing.invoice (id),
    CONSTRAINT ck_invoice_item_qty   CHECK (quantity > 0),
    CONSTRAINT ck_invoice_item_price CHECK (unit_price >= 0)
);
CREATE INDEX idx_invoice_item_invoice_id ON billing.invoice_item (invoice_id);
CREATE INDEX idx_invoice_item_product_id ON billing.invoice_item (product_id);
