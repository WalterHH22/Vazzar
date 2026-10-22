package com.app.vaazar.ecommerce.infrastructure.persistence;

import com.app.vaazar.fair.infrastructure.persistence.FairCompanyJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.FairProductJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairCompanyRepository;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairProductRepository;
import com.app.vaazar.inventory.infrastructure.persistence.CategoryJpaEntity;
import com.app.vaazar.inventory.infrastructure.persistence.InventoryCompanyJpaEntity;
import com.app.vaazar.inventory.infrastructure.persistence.InventoryProductJpaEntity;
import com.app.vaazar.inventory.infrastructure.persistence.SpringDataCategoryRepository;
import com.app.vaazar.inventory.infrastructure.persistence.SpringDataInventoryCompanyRepository;
import com.app.vaazar.inventory.infrastructure.persistence.SpringDataInventoryProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class CatalogDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogDataInitializer.class);

    private final SpringDataCategoryRepository inventoryCategoryRepo;
    private final SpringDataEcommerceCategoryRepository ecommerceCategoryRepo;
    private final SpringDataInventoryCompanyRepository inventoryCompanyRepo;
    private final SpringDataFairCompanyRepository fairCompanyRepo;
    private final SpringDataInventoryProductRepository inventoryProductRepo;
    private final SpringDataEcommerceProductRepository ecommerceProductRepo;
    private final SpringDataFairProductRepository fairProductRepo;

    public CatalogDataInitializer(
            SpringDataCategoryRepository inventoryCategoryRepo,
            SpringDataEcommerceCategoryRepository ecommerceCategoryRepo,
            SpringDataInventoryCompanyRepository inventoryCompanyRepo,
            SpringDataFairCompanyRepository fairCompanyRepo,
            SpringDataInventoryProductRepository inventoryProductRepo,
            SpringDataEcommerceProductRepository ecommerceProductRepo,
            SpringDataFairProductRepository fairProductRepo) {
        this.inventoryCategoryRepo = inventoryCategoryRepo;
        this.ecommerceCategoryRepo = ecommerceCategoryRepo;
        this.inventoryCompanyRepo = inventoryCompanyRepo;
        this.fairCompanyRepo = fairCompanyRepo;
        this.inventoryProductRepo = inventoryProductRepo;
        this.ecommerceProductRepo = ecommerceProductRepo;
        this.fairProductRepo = fairProductRepo;
    }

    @Override
    public void run(String... args) {
        // 1. Asegurar Categorías Base en ambos esquemas (Inventory y Ecommerce)
        UUID catCalzadoId = UUID.fromString("22222222-2222-2222-2222-222222222221");
        UUID catRopaId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        UUID catCafeId = UUID.fromString("22222222-2222-2222-2222-222222222223");
        UUID catAccesoriosId = UUID.fromString("22222222-2222-2222-2222-222222222224");

        seedCategory(catCalzadoId, "Calzado y Zapatillas");
        seedCategory(catRopaId, "Ropa y Moda");
        seedCategory(catCafeId, "Café y Gourmet");
        seedCategory(catAccesoriosId, "Accesorios y Joyería");

        if (ecommerceProductRepo.count() > 0) {
            log.info("El catálogo de Ecommerce en PostgreSQL ya cuenta con productos.");
            return;
        }

        log.info("Poblando catálogo de productos en PostgreSQL para la Tienda (Ecommerce)...");

        // 2. Empresa Principal
        UUID companyId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        if (!inventoryCompanyRepo.existsById(companyId)) {
            InventoryCompanyJpaEntity comp = new InventoryCompanyJpaEntity(
                    companyId,
                    UUID.randomUUID(),
                    "Vazzar Store Oficial",
                    "0614-010190-101-1",
                    "San Salvador, El Salvador",
                    "+503 2222-0000",
                    Instant.now()
            );
            inventoryCompanyRepo.save(comp);
        }

        if (!fairCompanyRepo.existsById(companyId)) {
            FairCompanyJpaEntity fairComp = new FairCompanyJpaEntity(
                    companyId,
                    "Vazzar Store Oficial",
                    "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400",
                    "contacto@vazzar.com"
            );
            fairCompanyRepo.save(fairComp);
        }

        // 3. Productos del catálogo
        List<ProductSeed> products = List.of(
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333301"),
                        companyId, catCalzadoId,
                        "Zapatillas Urbanas Retro",
                        "Calzado deportivo con suela ergonómica de máxima amortiguación y diseño vintage.",
                        new BigDecimal("69.99"), 45, true,
                        "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=800&auto=format&fit=crop"
                ),
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333302"),
                        companyId, catCalzadoId,
                        "Botas Clásicas de Cuero",
                        "Botas de cuero legítimo hechas a mano con costuras reforzadas resistentes al agua.",
                        new BigDecimal("94.50"), 28, true,
                        "https://images.unsplash.com/photo-1520639888713-7851133b1ed0?w=800&auto=format&fit=crop"
                ),
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333303"),
                        companyId, catRopaId,
                        "Chaqueta Denim Vintage",
                        "Chaqueta de mezclilla premium con forro térmico y botones metálicos de alta durabilidad.",
                        new BigDecimal("54.00"), 60, false,
                        "https://images.unsplash.com/photo-1576995853123-5a10305d93c0?w=800&auto=format&fit=crop"
                ),
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333304"),
                        companyId, catRopaId,
                        "Camisa de Lino Blanco",
                        "Camisa 100% lino fresco y transpirable, corte moderno ideal para clima cálido.",
                        new BigDecimal("38.00"), 35, true,
                        "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=800&auto=format&fit=crop"
                ),
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333305"),
                        companyId, catCafeId,
                        "Café Geisha Especial 500g",
                        "Granos de café de altura con notas cítricas y florales. Tueste medio artesanal.",
                        new BigDecimal("18.50"), 120, true,
                        "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=800&auto=format&fit=crop"
                ),
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333306"),
                        companyId, catCafeId,
                        "Miel de Flor de Azahar 350g",
                        "Miel 100% pura y orgánica recolectada en apiarios de bosque tropical.",
                        new BigDecimal("10.99"), 85, true,
                        "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=800&auto=format&fit=crop"
                ),
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333307"),
                        companyId, catAccesoriosId,
                        "Reloj Minimalista Negro",
                        "Movimiento de cuarzo de alta precisión con correa de cuero genuino y esfera de cristal mineral.",
                        new BigDecimal("82.00"), 20, false,
                        "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&auto=format&fit=crop"
                ),
                new ProductSeed(
                        UUID.fromString("33333333-3333-3333-3333-333333333308"),
                        companyId, catAccesoriosId,
                        "Bolso Tote Bag de Cuero",
                        "Bolso espacioso y versátil para uso diario con compartimento acolchado para laptop.",
                        new BigDecimal("42.50"), 40, true,
                        "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=800&auto=format&fit=crop"
                )
        );

        for (ProductSeed p : products) {
            // Guardar en inventory
            InventoryProductJpaEntity invProd = new InventoryProductJpaEntity(
                    p.id(), p.companyId(), p.name(), p.description(), p.stock(), p.fairEligible()
            );
            inventoryProductRepo.save(invProd);

            // Guardar en ecommerce (esquema ecommerce)
            EcommerceProductJpaEntity ecomProd = new EcommerceProductJpaEntity(
                    p.id(), p.companyId(), p.categoryId(), p.name(), p.description(),
                    p.price(), true, p.imageUrl()
            );
            ecommerceProductRepo.save(ecomProd);

            // Si es elegible para feria, guardar en fair (esquema fair)
            if (p.fairEligible()) {
                FairProductJpaEntity fairProd = new FairProductJpaEntity(
                        p.id(), p.companyId(), p.name(), p.description(), p.imageUrl()
                );
                fairProductRepo.save(fairProd);
            }
        }

        log.info("¡Se insertaron exitosamente 8 productos con imágenes y categorías en PostgreSQL!");
    }

    private void seedCategory(UUID id, String name) {
        if (!inventoryCategoryRepo.existsById(id)) {
            CategoryJpaEntity invCat = new CategoryJpaEntity(id, name);
            inventoryCategoryRepo.save(invCat);
        }
        if (!ecommerceCategoryRepo.existsById(id)) {
            EcommerceCategoryJpaEntity ecomCat = new EcommerceCategoryJpaEntity(id, name);
            ecommerceCategoryRepo.save(ecomCat);
        }
    }

    private record ProductSeed(
            UUID id,
            UUID companyId,
            UUID categoryId,
            String name,
            String description,
            BigDecimal price,
            int stock,
            boolean fairEligible,
            String imageUrl
    ) {}
}
