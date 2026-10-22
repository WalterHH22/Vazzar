-- Agregar columna created_by para asociar la categoría al usuario/vendedor creador
ALTER TABLE inventory.category ADD COLUMN created_by UUID;
CREATE INDEX idx_category_created_by ON inventory.category (created_by);
