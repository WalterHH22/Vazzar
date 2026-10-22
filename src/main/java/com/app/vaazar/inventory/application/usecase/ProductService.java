package com.app.vaazar.inventory.application.usecase;

import com.app.vaazar.inventory.domain.event.ProductCreatedEvent;
import com.app.vaazar.inventory.domain.event.ProductDeletedEvent;
import com.app.vaazar.inventory.infrastructure.persistence.InventoryCompanyJpaEntity;
import com.app.vaazar.inventory.infrastructure.persistence.InventoryProductJpaEntity;
import com.app.vaazar.inventory.infrastructure.persistence.SpringDataInventoryCompanyRepository;
import com.app.vaazar.inventory.infrastructure.persistence.SpringDataInventoryProductRepository;
import com.app.vaazar.inventory.infrastructure.web.dto.CreateProductRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final SpringDataInventoryProductRepository productRepository;
    private final SpringDataInventoryCompanyRepository companyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductService(SpringDataInventoryProductRepository productRepository,
                          SpringDataInventoryCompanyRepository companyRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.companyRepository = companyRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public InventoryProductJpaEntity createProduct(CreateProductRequest request) {
        // Asegurar que exista una empresa asociada en el esquema inventory (FK requerida)
        UUID companyId = request.getCompanyId();
        if (companyId == null || !companyRepository.existsById(companyId)) {
            InventoryCompanyJpaEntity defaultCompany = companyRepository.findFirstByOrderByCreatedAtAsc()
                    .orElseGet(() -> {
                        InventoryCompanyJpaEntity newComp = new InventoryCompanyJpaEntity(
                                UUID.randomUUID(),
                                UUID.randomUUID(),
                                "Empresa Principal Vazzar",
                                "0614-010190-101-1",
                                "San Salvador, El Salvador",
                                "+503 2222-0000",
                                Instant.now()
                        );
                        return companyRepository.save(newComp);
                    });
            companyId = defaultCompany.getId();
        }

        UUID productId = UUID.randomUUID();
        InventoryProductJpaEntity product = new InventoryProductJpaEntity(
                productId,
                companyId,
                request.getName(),
                request.getDescription(),
                request.getStock(),
                request.isFairEligible()
        );

        InventoryProductJpaEntity saved = productRepository.save(product);

        // Publicar evento de dominio para sincronizar ecommerce y fair sin acoplar esquemas
        ProductCreatedEvent event = new ProductCreatedEvent(
                saved.getId(),
                saved.getCompanyId(),
                request.getCategoryId(),
                saved.getName(),
                saved.getDescription(),
                request.getPrice(),
                saved.isFairEligible(),
                request.getImageUrl()
        );
        eventPublisher.publishEvent(event);

        return saved;
    }

    public List<InventoryProductJpaEntity> listAll() {
        return productRepository.findAll();
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            eventPublisher.publishEvent(new ProductDeletedEvent(productId));
        }
    }
}
