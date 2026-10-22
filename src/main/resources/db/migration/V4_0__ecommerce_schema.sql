-- =====================================================================
-- V4_0 | Bounded Context: ECOMMERCE
-- category/product son copias locales sincronizadas por eventos de Inventory
-- (mismo UUID que inventory.*). company_id y user_id sin FK.
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS ecommerce;

CREATE TABLE ecommerce.category (
    id    UUID         PRIMARY KEY,   -- mismo UUID que inventory.category
    name  VARCHAR(255) NOT NULL
);

CREATE TABLE ecommerce.product (
    id           UUID          PRIMARY KEY,   -- mismo UUID que inventory.product
    company_id   UUID          NOT NULL,      -- desnormalizado, sin FK
    category_id  UUID,
    name         VARCHAR(255)  NOT NULL,
    description  TEXT,
    price        DECIMAL(10,2) NOT NULL,
    available    BOOLEAN       NOT NULL DEFAULT true,
    image_url    VARCHAR(1000),
    CONSTRAINT fk_ecom_product_category FOREIGN KEY (category_id) REFERENCES ecommerce.category (id),
    CONSTRAINT ck_ecom_product_price    CHECK (price >= 0)
);
CREATE INDEX idx_ecom_product_company_id  ON ecommerce.product (company_id);
CREATE INDEX idx_ecom_product_category_id ON ecommerce.product (category_id);
-- Catálogo público: solo productos disponibles, filtrados por tienda/categoría
CREATE INDEX idx_ecom_product_catalog
    ON ecommerce.product (company_id, category_id) WHERE available = true;

CREATE TABLE ecommerce.cart (
    id          UUID         PRIMARY KEY,
    user_id     UUID         NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'open',
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT ck_cart_status CHECK (status IN ('open', 'confirmed', 'paid', 'cancelled'))
);
CREATE INDEX idx_cart_user_status ON ecommerce.cart (user_id, status);
-- Un usuario solo puede tener un carrito abierto a la vez
CREATE UNIQUE INDEX uq_cart_one_open_per_user ON ecommerce.cart (user_id) WHERE status = 'open';

CREATE TABLE ecommerce.cart_item (
    id          UUID          PRIMARY KEY,
    cart_id     UUID          NOT NULL,
    product_id  UUID          NOT NULL,
    quantity    INT           NOT NULL,
    unit_price  DECIMAL(10,2) NOT NULL,   -- precio al momento de agregar al carrito
    CONSTRAINT fk_cart_item_cart    FOREIGN KEY (cart_id)    REFERENCES ecommerce.cart (id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES ecommerce.product (id),
    CONSTRAINT uq_cart_item_product UNIQUE (cart_id, product_id),
    CONSTRAINT ck_cart_item_qty     CHECK (quantity > 0),
    CONSTRAINT ck_cart_item_price   CHECK (unit_price >= 0)
);
CREATE INDEX idx_cart_item_product_id ON ecommerce.cart_item (product_id);
