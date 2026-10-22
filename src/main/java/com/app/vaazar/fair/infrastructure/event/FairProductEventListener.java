package com.app.vaazar.fair.infrastructure.event;

import com.app.vaazar.fair.infrastructure.persistence.FairCompanyJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.FairProductJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairCompanyRepository;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairProductRepository;
import com.app.vaazar.inventory.domain.event.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class FairProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(FairProductEventListener.class);

    private final SpringDataFairCompanyRepository companyRepository;
    private final SpringDataFairProductRepository productRepository;

    public FairProductEventListener(SpringDataFairCompanyRepository companyRepository,
                                    SpringDataFairProductRepository productRepository) {
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
    }

    @EventListener
    @Transactional
    public void onProductCreated(ProductCreatedEvent event) {
        if (!event.isFairEligible()) {
            log.info("[FAIR-EVENT] Producto id: '{}' no es elegible para feria (fair_eligible=false). Ignorando.",
                    event.getProductId());
            return;
        }

        log.info("[FAIR-EVENT] Sincronizando producto elegible para feria -> id: {}, nombre: '{}'",
                event.getProductId(), event.getName());

        // Aseguramos que la empresa exista en el esquema fair (para respetar FK local)
        if (!companyRepository.existsById(event.getCompanyId())) {
            FairCompanyJpaEntity company = new FairCompanyJpaEntity(
                    event.getCompanyId(),
                    "Empresa Expositora",
                    null,
                    null
            );
            companyRepository.save(company);
        }

        FairProductJpaEntity product = new FairProductJpaEntity(
                event.getProductId(),
                event.getCompanyId(),
                event.getName(),
                event.getDescription(),
                event.getImageUrl()
        );

        productRepository.save(product);
        log.info("[FAIR-EVENT] Producto sincronizado con éxito en 'fair.product'");
    }
}
