-- =====================================================================
-- V11_0 | Seed: Ampliación del Catálogo de Productos por Categoría
-- =====================================================================

-- 1. Normalización de nombres de categorías con acentos limpios
UPDATE inventory.category SET name = 'Café y Gourmet' WHERE id = '22222222-2222-2222-2222-222222222223';
UPDATE ecommerce.category SET name = 'Café y Gourmet' WHERE id = '22222222-2222-2222-2222-222222222223';

UPDATE inventory.category SET name = 'Accesorios y Joyería' WHERE id = '22222222-2222-2222-2222-222222222224';
UPDATE ecommerce.category SET name = 'Accesorios y Joyería' WHERE id = '22222222-2222-2222-2222-222222222224';

UPDATE inventory.category SET name = 'Tecnología' WHERE id = '7d1f631b-9a13-49af-93fd-6bcf527e9ed5';
UPDATE ecommerce.category SET name = 'Tecnología' WHERE id = '7d1f631b-9a13-49af-93fd-6bcf527e9ed5';

UPDATE inventory.category SET name = 'Iluminación' WHERE id = '5bf42a59-6a32-4e56-8998-d7af166d3766';
UPDATE ecommerce.category SET name = 'Iluminación' WHERE id = '5bf42a59-6a32-4e56-8998-d7af166d3766';

-- =====================================================================
-- 2. Productos en Calzado y Zapatillas
-- =====================================================================
INSERT INTO inventory.product (id, company_id, name, description, stock, fair_eligible, created_at, updated_at)
VALUES
(
    'a1111111-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    'Mocasines de Gamuza Italiana',
    'Calzado formal casual elaborado en gamuza aterciopelada con plantilla acolchada y suela de goma antideslizante.',
    35, false, now(), now()
),
(
    'a1111111-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    'Sandalias de Cuero Artesanal',
    'Sandalias de tiras de cuero vacuno curtido al natural con plantilla ergonómica de corcho.',
    50, false, now(), now()
),
(
    'a1111111-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    'Zapatillas Deportivas Running Air',
    'Malla transpirable ultraligera con cámara de aire reactiva para alto rendimiento.',
    60, false, now(), now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ecommerce.product (id, company_id, category_id, name, description, price, available, image_url)
VALUES
(
    'a1111111-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222221',
    'Mocasines de Gamuza Italiana',
    'Calzado formal casual elaborado en gamuza aterciopelada con plantilla acolchada y suela de goma antideslizante.',
    85.00, true,
    'https://images.unsplash.com/photo-1533867617858-e7b97e060509?w=800&auto=format&fit=crop'
),
(
    'a1111111-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222221',
    'Sandalias de Cuero Artesanal',
    'Sandalias de tiras de cuero vacuno curtido al natural con plantilla ergonómica de corcho.',
    45.00, true,
    'https://images.unsplash.com/photo-1603808033192-082d6919d3e1?w=800&auto=format&fit=crop'
),
(
    'a1111111-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222221',
    'Zapatillas Deportivas Running Air',
    'Malla transpirable ultraligera con cámara de aire reactiva para alto rendimiento.',
    78.50, true,
    'https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?w=800&auto=format&fit=crop'
)
ON CONFLICT (id) DO NOTHING;

-- =====================================================================
-- 3. Productos en Ropa y Moda
-- =====================================================================
INSERT INTO inventory.product (id, company_id, name, description, stock, fair_eligible, created_at, updated_at)
VALUES
(
    'b2222222-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    'Suéter Tejido de Lana Merino',
    'Suéter clásico de cuello redondo en suave lana merino transpirable y cálida.',
    40, false, now(), now()
),
(
    'b2222222-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    'Vestido Floral de Seda Suave',
    'Vestido midi con estampado botánico y corte estilizado de caída ligera.',
    25, false, now(), now()
),
(
    'b2222222-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    'Pantalón Chino Slim Fit',
    'Pantalón de gabardina de algodón con spandex para mayor libertad de movimiento.',
    45, false, now(), now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ecommerce.product (id, company_id, category_id, name, description, price, available, image_url)
VALUES
(
    'b2222222-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222222',
    'Suéter Tejido de Lana Merino',
    'Suéter clásico de cuello redondo en suave lana merino transpirable y cálida.',
    62.00, true,
    'https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=800&auto=format&fit=crop'
),
(
    'b2222222-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222222',
    'Vestido Floral de Seda Suave',
    'Vestido midi con estampado botánico y corte estilizado de caída ligera.',
    74.99, true,
    'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=800&auto=format&fit=crop'
),
(
    'b2222222-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222222',
    'Pantalón Chino Slim Fit',
    'Pantalón de gabardina de algodón con spandex para mayor libertad de movimiento.',
    46.00, true,
    'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?w=800&auto=format&fit=crop'
)
ON CONFLICT (id) DO NOTHING;

-- =====================================================================
-- 4. Productos en Café y Gourmet
-- =====================================================================
INSERT INTO inventory.product (id, company_id, name, description, stock, fair_eligible, created_at, updated_at)
VALUES
(
    'c3333333-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    'Chocolate Oscuro 72% Cacao Criollo',
    'Tableta de chocolate artesanal con notas tostadas de avellana y vainilla natural.',
    80, false, now(), now()
),
(
    'c3333333-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    'Café Bourbon Rojo de Altura (1kg)',
    'Café cultivado en estricta altura (1,600 msnm) con cuerpo sedoso y acidez equilibrada.',
    65, false, now(), now()
),
(
    'c3333333-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    'Mermelada Artesanal de Frutos Silvestres',
    'Mermelada a fuego lento de mora, fresa y frambuesa sin conservantes artificiales.',
    50, false, now(), now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ecommerce.product (id, company_id, category_id, name, description, price, available, image_url)
VALUES
(
    'c3333333-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222223',
    'Chocolate Oscuro 72% Cacao Criollo',
    'Tableta de chocolate artesanal con notas tostadas de avellana y vainilla natural.',
    6.50, true,
    'https://images.unsplash.com/photo-1548907040-4baa42d10919?w=800&auto=format&fit=crop'
),
(
    'c3333333-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222223',
    'Café Bourbon Rojo de Altura (1kg)',
    'Café cultivado en estricta altura (1,600 msnm) con cuerpo sedoso y acidez equilibrada.',
    24.00, true,
    'https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=800&auto=format&fit=crop'
),
(
    'c3333333-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222223',
    'Mermelada Artesanal de Frutos Silvestres',
    'Mermelada a fuego lento de mora, fresa y frambuesa sin conservantes artificiales.',
    5.75, true,
    'https://images.unsplash.com/photo-1568651347573-04fd52b1ec31?w=800&auto=format&fit=crop'
)
ON CONFLICT (id) DO NOTHING;

-- =====================================================================
-- 5. Productos en Accesorios y Joyería
-- =====================================================================
INSERT INTO inventory.product (id, company_id, name, description, stock, fair_eligible, created_at, updated_at)
VALUES
(
    'd4444444-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    'Gafas de Sol Polarizadas Aviador',
    'Montura dorada ultraligera con lentes polarizados de protección UV400 completa.',
    30, false, now(), now()
),
(
    'd4444444-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    'Collar de Plata 925 con Dije Minimalista',
    'Cadena veneciana en plata esterlina pulida con dije de circonia cúbica.',
    25, false, now(), now()
),
(
    'd4444444-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    'Billetera de Cuero Compacta RFID',
    'Billetera delgada con compartimentos para 8 tarjetas y blindaje anti-escaneo RFID.',
    45, false, now(), now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ecommerce.product (id, company_id, category_id, name, description, price, available, image_url)
VALUES
(
    'd4444444-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222224',
    'Gafas de Sol Polarizadas Aviador',
    'Montura dorada ultraligera con lentes polarizados de protección UV400 completa.',
    49.99, true,
    'https://images.unsplash.com/photo-1511499767150-a48a237f0083?w=800&auto=format&fit=crop'
),
(
    'd4444444-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222224',
    'Collar de Plata 925 con Dije Minimalista',
    'Cadena veneciana en plata esterlina pulida con dije de circonia cúbica.',
    58.00, true,
    'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=800&auto=format&fit=crop'
),
(
    'd4444444-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222224',
    'Billetera de Cuero Compacta RFID',
    'Billetera delgada con compartimentos para 8 tarjetas y blindaje anti-escaneo RFID.',
    32.00, true,
    'https://images.unsplash.com/photo-1627123424574-724758594e93?w=800&auto=format&fit=crop'
)
ON CONFLICT (id) DO NOTHING;

-- =====================================================================
-- 6. Productos en Tecnología
-- =====================================================================
INSERT INTO inventory.product (id, company_id, name, description, stock, fair_eligible, created_at, updated_at)
VALUES
(
    'e5555555-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    'Audífonos Inalámbricos Pro ANC',
    'Cancelación activa de ruido, audio espacial 3D y estuche con carga inalámbrica Qi.',
    40, false, now(), now()
),
(
    'e5555555-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    'Smartwatch Deportivo Fitness 50m',
    'Pantalla AMOLED, monitoreo de ritmo cardíaco, oxigenación SpO2 y GPS para running.',
    35, false, now(), now()
),
(
    'e5555555-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    'Parlante Bluetooth Resistente IPX7',
    'Sonido envolvente 360 grados de alta fidelidad con 16 horas de batería continua.',
    50, false, now(), now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ecommerce.product (id, company_id, category_id, name, description, price, available, image_url)
VALUES
(
    'e5555555-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    '7d1f631b-9a13-49af-93fd-6bcf527e9ed5',
    'Audífonos Inalámbricos Pro ANC',
    'Cancelación activa de ruido, audio espacial 3D y estuche con carga inalámbrica Qi.',
    119.00, true,
    'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=800&auto=format&fit=crop'
),
(
    'e5555555-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    '7d1f631b-9a13-49af-93fd-6bcf527e9ed5',
    'Smartwatch Deportivo Fitness 50m',
    'Pantalla AMOLED, monitoreo de ritmo cardíaco, oxigenación SpO2 y GPS para running.',
    89.99, true,
    'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&auto=format&fit=crop'
),
(
    'e5555555-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    '7d1f631b-9a13-49af-93fd-6bcf527e9ed5',
    'Parlante Bluetooth Resistente IPX7',
    'Sonido envolvente 360 grados de alta fidelidad con 16 horas de batería continua.',
    59.50, true,
    'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=800&auto=format&fit=crop'
)
ON CONFLICT (id) DO NOTHING;

-- =====================================================================
-- 7. Productos en Iluminación
-- =====================================================================
INSERT INTO inventory.product (id, company_id, name, description, stock, fair_eligible, created_at, updated_at)
VALUES
(
    'f6666666-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    'Lámpara de Mesa Nórdica Minimalista',
    'Base de roble macizo con pantalla de lino difusora de luz cálida regulable.',
    30, false, now(), now()
),
(
    'f6666666-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    'Foco LED Inteligente WiFi RGB+W',
    'Control por smartphone y comandos de voz, 16 millones de colores y temporizador.',
    70, false, now(), now()
),
(
    'f6666666-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    'Lámpara Colgante Geométrica Industrial',
    'Estructura de acero forjado negro mate con cable textil reforzado estilo loft vintage.',
    25, false, now(), now()
)
ON CONFLICT (id) DO NOTHING;

INSERT INTO ecommerce.product (id, company_id, category_id, name, description, price, available, image_url)
VALUES
(
    'f6666666-0000-0000-0000-000000000001',
    '11111111-1111-1111-1111-111111111111',
    '5bf42a59-6a32-4e56-8998-d7af166d3766',
    'Lámpara de Mesa Nórdica Minimalista',
    'Base de roble macizo con pantalla de lino difusora de luz cálida regulable.',
    48.00, true,
    'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=800&auto=format&fit=crop'
),
(
    'f6666666-0000-0000-0000-000000000002',
    '11111111-1111-1111-1111-111111111111',
    '5bf42a59-6a32-4e56-8998-d7af166d3766',
    'Foco LED Inteligente WiFi RGB+W',
    'Control por smartphone y comandos de voz, 16 millones de colores y temporizador.',
    16.99, true,
    'https://images.unsplash.com/photo-1550985616-10810253b84d?w=800&auto=format&fit=crop'
),
(
    'f6666666-0000-0000-0000-000000000003',
    '11111111-1111-1111-1111-111111111111',
    '5bf42a59-6a32-4e56-8998-d7af166d3766',
    'Lámpara Colgante Geométrica Industrial',
    'Estructura de acero forjado negro mate con cable textil reforzado estilo loft vintage.',
    65.00, true,
    'https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?w=800&auto=format&fit=crop'
)
ON CONFLICT (id) DO NOTHING;
