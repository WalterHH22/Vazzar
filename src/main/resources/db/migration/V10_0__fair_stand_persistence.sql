-- =====================================================================
-- V10_0 | Bounded Context: FAIR
-- Persistencia de Stands feriales por comerciante y productos de vitrina
-- =====================================================================

CREATE TABLE IF NOT EXISTS fair.stand (
    id             UUID         PRIMARY KEY,
    merchant_email VARCHAR(255) NOT NULL,
    merchant_name  VARCHAR(255) NOT NULL,
    stand_number   VARCHAR(50)  NOT NULL,
    name           VARCHAR(255) NOT NULL,
    pavilion       VARCHAR(100) NOT NULL,
    canopy_color   VARCHAR(50)  NOT NULL DEFAULT '#6366f1',
    description    TEXT,
    logo_url       VARCHAR(1000),
    created_at     TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_stand_merchant_email ON fair.stand (lower(merchant_email));

CREATE TABLE IF NOT EXISTS fair.stand_product (
    id            UUID          PRIMARY KEY,
    stand_id      UUID          NOT NULL,
    product_id    UUID,
    name          VARCHAR(255)  NOT NULL,
    description   TEXT,
    price         DECIMAL(10,2),
    stock         INT           DEFAULT 50,
    image_url     VARCHAR(1000),
    created_at    TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT fk_stand_product_stand FOREIGN KEY (stand_id) REFERENCES fair.stand (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_stand_product_stand_id ON fair.stand_product (stand_id);

-- Semilla de Stands Iniciales
INSERT INTO fair.stand (id, merchant_email, merchant_name, stand_number, name, pavilion, canopy_color, description, logo_url)
VALUES
(
    '11111111-2222-3333-4444-555555555501',
    'harold_orellana@siman.com',
    'Harold Orellana',
    'STAND #01',
    'Puesto Siman Store',
    'Tecnología & Móviles',
    '#6366f1',
    'Puesto oficial de Siman Store en la feria. Especialistas en telefonía móvil, computación y tecnología de última generación.',
    'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=300'
),
(
    '11111111-2222-3333-4444-555555555502',
    'calzado@vazzar.com',
    'Maestros Zapateros Vazzar',
    'STAND #02',
    'Puesto Calzado & Cuero Artesanal',
    'Calzado & Cuero',
    '#ea580c',
    'Taller artesanal de calzado deportivo retro y botas de cuero legítimo cosidas a mano con acabados impermeables.',
    'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300'
),
(
    '11111111-2222-3333-4444-555555555503',
    'cafegourmet@vazzar.com',
    'Cooperativa Agrícola de Altura',
    'STAND #03',
    'Puesto Finca Cafetalera El Roble',
    'Café & Gourmet',
    '#059669',
    'Café Geisha de estricta altura con notas cítricas y miel de azahar 100% pura recolectada de bosques tropicales.',
    'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=300'
),
(
    '11111111-2222-3333-4444-555555555504',
    'moda@vazzar.com',
    'Artesanos Textiles Locales',
    'STAND #04',
    'Puesto Taller Textil & Confecciones',
    'Moda & Accesorios',
    '#0284c7',
    'Confección en lino puro transpirable para clima cálido y bolsos tote bag de cuero de uso diario.',
    'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=300'
)
ON CONFLICT (id) DO NOTHING;

-- Semilla de Productos de Vitrina para cada Stand
INSERT INTO fair.stand_product (id, stand_id, product_id, name, description, price, stock, image_url)
VALUES
(
    '60995792-37e9-436b-b1ec-20b7b4d25429',
    '11111111-2222-3333-4444-555555555501',
    '60995792-37e9-436b-b1ec-20b7b4d25429',
    'Celular Samsung Galaxy A37',
    '8GB RAM, 256GB Almacenamiento, Pantalla Super AMOLED 6.7 120Hz, Cámara cuádruple de 64MP.',
    349.99,
    25,
    'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500'
),
(
    '22222222-0000-0000-0000-000000000001',
    '11111111-2222-3333-4444-555555555502',
    NULL,
    'Zapatillas Urbanas Retro',
    'Diseño clásico en cuero suave con suela vulcanizada de alta durabilidad y plantilla acolchada ergonómica.',
    59.99,
    30,
    'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500'
),
(
    '22222222-0000-0000-0000-000000000002',
    '11111111-2222-3333-4444-555555555502',
    NULL,
    'Botas Clásicas de Cuero',
    'Cuero vacuno tratado resistente al agua, costuras reforzadas a mano y suela antideslizante todoterreno.',
    89.50,
    15,
    'https://images.unsplash.com/photo-1608256246200-53e635b5b65f?w=500'
),
(
    '33333333-0000-0000-0000-000000000001',
    '11111111-2222-3333-4444-555555555503',
    NULL,
    'Café Geisha Especial 500g',
    'Grano entero de estricta altura (1,800 msnm), tueste medio artesanal con notas florales y jazmín.',
    18.50,
    40,
    'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=500'
),
(
    '33333333-0000-0000-0000-000000000002',
    '11111111-2222-3333-4444-555555555503',
    NULL,
    'Miel de Flor de Azahar 350g',
    'Miel 100% pura y cruda recolectada en floración de cítricos, textura cremosa y sabor delicado.',
    8.25,
    50,
    'https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=500'
),
(
    '44444444-0000-0000-0000-000000000001',
    '11111111-2222-3333-4444-555555555504',
    NULL,
    'Camisa de Lino Blanco',
    '100% lino natural transpirable, corte regular fit ideal para clima cálido y estilo relajado contemporáneo.',
    34.00,
    20,
    'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500'
),
(
    '44444444-0000-0000-0000-000000000002',
    '11111111-2222-3333-4444-555555555504',
    NULL,
    'Bolso Tote Bag de Cuero',
    'Bolso espacioso de cuero genuino curtido vegetal con asas reforzadas y compartimento para laptop.',
    45.00,
    18,
    'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=500'
)
ON CONFLICT (id) DO NOTHING;
