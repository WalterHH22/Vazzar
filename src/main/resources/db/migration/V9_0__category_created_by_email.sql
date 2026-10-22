-- Guardar el email del creador directamente en inventory.category
ALTER TABLE inventory.category ADD COLUMN created_by_email VARCHAR(255);
