-- Al eliminar una categoría de ecommerce, desvincularla de los productos (category_id a NULL)
ALTER TABLE ecommerce.product
    DROP CONSTRAINT IF EXISTS fk_ecom_product_category,
    ADD CONSTRAINT fk_ecom_product_category
        FOREIGN KEY (category_id) REFERENCES ecommerce.category (id) ON DELETE SET NULL;

-- Al eliminar una categoría de inventory, eliminar subcategorías hijas en cascada si las hay
ALTER TABLE inventory.subcategory
    DROP CONSTRAINT IF EXISTS fk_subcategory_category,
    ADD CONSTRAINT fk_subcategory_category
        FOREIGN KEY (category_id) REFERENCES inventory.category (id) ON DELETE CASCADE;
