package com.app.vaazar.ecommerce.infrastructure.event;

import com.app.vaazar.ecommerce.infrastructure.persistence.EcommerceProductJpaEntity;
import com.app.vaazar.ecommerce.infrastructure.persistence.SpringDataEcommerceProductRepository;
import com.app.vaazar.inventory.domain.event.ProductCreatedEvent;
import com.app.vaazar.inventory.domain.event.ProductDeletedEvent;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EcommerceProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(EcommerceProductEventListener.class);

    private final SpringDataEcommerceProductRepository productRepository;
    private final EntityManager entityManager;

    public EcommerceProductEventListener(SpringDataEcommerceProductRepository productRepository) {
        this(productRepository, null);
    }

    @org.springframework.beans.factory.annotation.Autowired
    public EcommerceProductEventListener(SpringDataEcommerceProductRepository productRepository,
                                         @org.springframework.beans.factory.annotation.Autowired(required = false) EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
    }

    @EventListener
    @Transactional
    public void onProductCreated(ProductCreatedEvent event) {
        log.info("[ECOMMERCE-EVENT] Sincronizando producto desde Inventory -> id: {}, nombre: '{}', precio: ${}",
                event.getProductId(), event.getName(), event.getPrice());

        EcommerceProductJpaEntity entity = new EcommerceProductJpaEntity(
                event.getProductId(),
                event.getCompanyId(),
                event.getCategoryId(),
                event.getName(),
                event.getDescription(),
                event.getPrice(),
                true,
                event.getImageUrl()
        );

        productRepository.save(entity);
        log.info("[ECOMMERCE-EVENT] Producto guardado en catálogo 'ecommerce.product'");
    }

    @EventListener
    @Transactional
    public void onProductDeleted(ProductDeletedEvent event) {
        log.info("[ECOMMERCE-EVENT] Eliminando producto desde Inventory -> id: {}", event.getProductId());
        if (entityManager != null) {
            try {
                entityManager.createNativeQuery("DELETE FROM ecommerce.cart_item WHERE product_id = :pid")
                        .setParameter("pid", event.getProductId())
                        .executeUpdate();
            } catch (Exception ignored) {
            }
        }
        productRepository.deleteById(event.getProductId());
        log.info("[ECOMMERCE-EVENT] Producto eliminado exitosamente de 'ecommerce.product'");
    }
}
