-- Permitir que un producto en inventory no requiera obligatoriamente subcategoría
ALTER TABLE inventory.product ALTER COLUMN subcategory_id DROP NOT NULL;
