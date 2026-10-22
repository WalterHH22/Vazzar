package com.app.vaazar.ecommerce.infrastructure.event;

import com.app.vaazar.ecommerce.domain.model.EcommerceCategory;
import com.app.vaazar.ecommerce.domain.port.EcommerceCategoryRepositoryPort;
import com.app.vaazar.inventory.domain.event.CategoryCreatedEvent;
import com.app.vaazar.inventory.domain.event.CategoryUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EcommerceCategoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(EcommerceCategoryEventListener.class);

    private final EcommerceCategoryRepositoryPort repositoryPort;

    public EcommerceCategoryEventListener(EcommerceCategoryRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @EventListener
    @Transactional
    public void onCategoryCreated(CategoryCreatedEvent event) {
        log.info("[ECOMMERCE-EVENT] Sincronizando categoría desde Inventory -> id: {}, nombre: '{}'",
                event.getCategoryId(), event.getName());

        EcommerceCategory category = new EcommerceCategory(event.getCategoryId(), event.getName());
        repositoryPort.save(category);

        log.info("[ECOMMERCE-EVENT] Sincronización exitosa en esquema 'ecommerce.category'");
    }

    @EventListener
    @Transactional
    public void onCategoryUpdated(CategoryUpdatedEvent event) {
        log.info("[ECOMMERCE-EVENT] Actualizando categoría desde Inventory -> id: {}, nuevo nombre: '{}'",
                event.getCategoryId(), event.getName());

        EcommerceCategory category = new EcommerceCategory(event.getCategoryId(), event.getName());
        repositoryPort.save(category);

        log.info("[ECOMMERCE-EVENT] Actualización exitosa en esquema 'ecommerce.category'");
    }
}
