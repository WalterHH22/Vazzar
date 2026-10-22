-- =====================================================================
-- Migración V12_0: Corrección de imágenes apropiadas por descripción
-- y limpieza de productos duplicados/incompletos
-- =====================================================================

-- 1. Eliminar producto duplicado sin imagen ni categoría si existe
DELETE FROM ecommerce.product WHERE id = '9356d9c1-4fb0-46a5-8759-c87fab213967';
DELETE FROM inventory.product WHERE id = '9356d9c1-4fb0-46a5-8759-c87fab213967';

-- 2. TECNOLOGÍA: Smartwatch Deportivo Fitness (foto real de reloj smartwatch digital deportivo)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=800&auto=format&fit=crop',
    description = 'Smartwatch digital deportivo con pantalla táctil OLED, monitor de ritmo cardíaco, podómetro y correa de silicona negra.'
WHERE name ILIKE '%Smartwatch%';

UPDATE inventory.product
SET description = 'Smartwatch digital deportivo con pantalla táctil OLED, monitor de ritmo cardíaco, podómetro y correa de silicona negra.'
WHERE name ILIKE '%Smartwatch%';

-- 3. ACCESORIOS: Reloj Minimalista Negro (foto de reloj analógico negro mate)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800&auto=format&fit=crop',
    description = 'Reloj analógico minimalista con esfera negra mate, agujas en contraste y correa de cuero negro genuino.'
WHERE name ILIKE '%Reloj Minimalista Negro%';

UPDATE inventory.product
SET description = 'Reloj analógico minimalista con esfera negra mate, agujas en contraste y correa de cuero negro genuino.'
WHERE name ILIKE '%Reloj Minimalista Negro%';

-- 4. TECNOLOGÍA: Smartphone Samsung Galaxy
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=800&auto=format&fit=crop',
    description = 'Smartphone moderno con pantalla Super AMOLED Full HD+, procesador octa-core y módulo multicámara de 64MP.'
WHERE name ILIKE '%Samsung Galaxy%';

UPDATE inventory.product
SET description = 'Smartphone moderno con pantalla Super AMOLED Full HD+, procesador octa-core y módulo multicámara de 64MP.'
WHERE name ILIKE '%Samsung Galaxy%';

-- 5. TECNOLOGÍA: Audífonos Inalámbricos Pro ANC (auriculares premium over-ear)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&auto=format&fit=crop',
    description = 'Auriculares inalámbricos over-ear con cancelación activa de ruido híbrida y almohadillas viscoelásticas.'
WHERE name ILIKE '%Audífonos Inalámbricos%';

UPDATE inventory.product
SET description = 'Auriculares inalámbricos over-ear con cancelación activa de ruido híbrida y almohadillas viscoelásticas.'
WHERE name ILIKE '%Audífonos Inalámbricos%';

-- 6. ROPA: Suéter Tejido de Lana Merino (suéter real de punto)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=800&auto=format&fit=crop',
    description = 'Suéter de punto grueso tejido en lana merino 100% natural, cuello redondo y textura suave y abrigadora.'
WHERE name ILIKE '%Suéter Tejido%';

UPDATE inventory.product
SET description = 'Suéter de punto grueso tejido en lana merino 100% natural, cuello redondo y textura suave y abrigadora.'
WHERE name ILIKE '%Suéter Tejido%';

-- 7. ROPA: Pantalón Chino Slim Fit
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=800&auto=format&fit=crop',
    description = 'Pantalón chino corte slim fit confeccionado en algodón elástico beige, ideal para oficina y estilo casual.'
WHERE name ILIKE '%Pantalón Chino%';

UPDATE inventory.product
SET description = 'Pantalón chino corte slim fit confeccionado en algodón elástico beige, ideal para oficina y estilo casual.'
WHERE name ILIKE '%Pantalón Chino%';

-- 8. GOURMET: Miel de Flor de Azahar (frasco de miel pura con paleta mielera)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1558642452-9d2a7deb7f62?w=800&auto=format&fit=crop',
    description = 'Miel pura y artesanal de flor de azahar en frasco de vidrio con paleta de madera mielera tradicional.'
WHERE name ILIKE '%Miel de Flor%';

UPDATE inventory.product
SET description = 'Miel pura y artesanal de flor de azahar en frasco de vidrio con paleta de madera mielera tradicional.'
WHERE name ILIKE '%Miel de Flor%';

-- 9. GOURMET: Chocolate Oscuro 72% Cacao Criollo (barra de chocolate negro real)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1511381939415-e44015466834?w=800&auto=format&fit=crop',
    description = 'Tableta de chocolate negro 72% cacao fino de aroma, elaborado con granos criollos seleccionados.'
WHERE name ILIKE '%Chocolate Oscuro%';

UPDATE inventory.product
SET description = 'Tableta de chocolate negro 72% cacao fino de aroma, elaborado con granos criollos seleccionados.'
WHERE name ILIKE '%Chocolate Oscuro%';

-- 10. GOURMET: Mermelada Artesanal de Frutos Silvestres (tarro de mermelada de frutos rojos)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1502741224143-90386d7f8c82?w=800&auto=format&fit=crop',
    description = 'Mermelada natural de moras y frambuesas silvestres cocinadas a fuego lento en jarra artesanal de cristal.'
WHERE name ILIKE '%Mermelada Artesanal%';

UPDATE inventory.product
SET description = 'Mermelada natural de moras y frambuesas silvestres cocinadas a fuego lento en jarra artesanal de cristal.'
WHERE name ILIKE '%Mermelada Artesanal%';

-- 11. CALZADO: Sandalias de Cuero Artesanal (sandalias de tiras de cuero)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1562273138-f46be4ebdf33?w=800&auto=format&fit=crop',
    description = 'Sandalias de tiras de cuero marrón curtido vegetal hechas a mano con suela ergonómica antideslizante.'
WHERE name ILIKE '%Sandalias de Cuero%';

UPDATE inventory.product
SET description = 'Sandalias de tiras de cuero marrón curtido vegetal hechas a mano con suela ergonómica antideslizante.'
WHERE name ILIKE '%Sandalias de Cuero%';

-- 12. CALZADO: Mocasines de Gamuza Italiana (loafers clásicos de gamuza)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1614252235316-8c857d38b5f4?w=800&auto=format&fit=crop',
    description = 'Mocasines clásicos tipo loafer en fina gamuza italiana con costuras artesanales y plantilla acolchada.'
WHERE name ILIKE '%Mocasines de Gamuza%';

UPDATE inventory.product
SET description = 'Mocasines clásicos tipo loafer en fina gamuza italiana con costuras artesanales y plantilla acolchada.'
WHERE name ILIKE '%Mocasines de Gamuza%';

-- 13. ILUMINACIÓN: Foco LED Inteligente WiFi RGB+W (bombilla LED inteligente iluminada)
UPDATE ecommerce.product
SET image_url = 'https://images.unsplash.com/photo-1540932239986-30128078f3c5?w=800&auto=format&fit=crop',
    description = 'Bombilla LED inteligente WiFi RGB+W multicolor regulable, controlable mediante app móvil y comandos de voz.'
WHERE name ILIKE '%Foco LED Inteligente%';

UPDATE inventory.product
SET description = 'Bombilla LED inteligente WiFi RGB+W multicolor regulable, controlable mediante app móvil y comandos de voz.'
WHERE name ILIKE '%Foco LED Inteligente%';
