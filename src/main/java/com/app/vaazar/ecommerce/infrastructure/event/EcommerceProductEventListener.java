package com.app.vaazar.ecommerce.infrastructure.event;

import com.app.vaazar.ecommerce.infrastructure.persistence.EcommerceProductJpaEntity;
import com.app.vaazar.ecommerce.infrastructure.persistence.SpringDataEcommerceProductRepository;
import com.app.vaazar.inventory.domain.event.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EcommerceProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(EcommerceProductEventListener.class);

    private final SpringDataEcommerceProductRepository productRepository;

    public EcommerceProductEventListener(SpringDataEcommerceProductRepository productRepository) {
        this.productRepository = productRepository;
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
}
