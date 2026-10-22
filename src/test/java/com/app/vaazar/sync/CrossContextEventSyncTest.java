package com.app.vaazar.sync;

import com.app.vaazar.ecommerce.domain.model.EcommerceCategory;
import com.app.vaazar.ecommerce.domain.port.EcommerceCategoryRepositoryPort;
import com.app.vaazar.ecommerce.infrastructure.event.EcommerceCategoryEventListener;
import com.app.vaazar.ecommerce.infrastructure.event.EcommerceProductEventListener;
import com.app.vaazar.ecommerce.infrastructure.persistence.EcommerceProductJpaEntity;
import com.app.vaazar.ecommerce.infrastructure.persistence.SpringDataEcommerceProductRepository;
import com.app.vaazar.fair.infrastructure.event.FairProductEventListener;
import com.app.vaazar.fair.infrastructure.persistence.FairCompanyJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.FairProductJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairCompanyRepository;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairProductRepository;
import com.app.vaazar.inventory.domain.event.CategoryCreatedEvent;
import com.app.vaazar.inventory.domain.event.ProductCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrossContextEventSyncTest {

    private EcommerceCategoryRepositoryPort ecommerceCategoryRepository;
    private EcommerceCategoryEventListener ecommerceCategoryListener;

    private SpringDataEcommerceProductRepository ecommerceProductRepository;
    private EcommerceProductEventListener ecommerceProductListener;

    private SpringDataFairCompanyRepository fairCompanyRepository;
    private SpringDataFairProductRepository fairProductRepository;
    private FairProductEventListener fairProductListener;

    @BeforeEach
    void setUp() {
        ecommerceCategoryRepository = mock(EcommerceCategoryRepositoryPort.class);
        ecommerceCategoryListener = new EcommerceCategoryEventListener(ecommerceCategoryRepository);

        ecommerceProductRepository = mock(SpringDataEcommerceProductRepository.class);
        ecommerceProductListener = new EcommerceProductEventListener(ecommerceProductRepository);

        fairCompanyRepository = mock(SpringDataFairCompanyRepository.class);
        fairProductRepository = mock(SpringDataFairProductRepository.class);
        fairProductListener = new FairProductEventListener(fairCompanyRepository, fairProductRepository);
    }

    @Test
    void shouldSyncCategoryToEcommerceWhenCreatedInInventory() {
        UUID categoryId = UUID.randomUUID();
        CategoryCreatedEvent event = new CategoryCreatedEvent(categoryId, "Ropa y Moda");

        ecommerceCategoryListener.onCategoryCreated(event);

        ArgumentCaptor<EcommerceCategory> captor = ArgumentCaptor.forClass(EcommerceCategory.class);
        verify(ecommerceCategoryRepository, times(1)).save(captor.capture());

        EcommerceCategory saved = captor.getValue();
        assertEquals(categoryId, saved.getId());
        assertEquals("Ropa y Moda", saved.getName());
    }

    @Test
    void shouldSyncProductToEcommerceAndFairWhenFairEligibleIsTrue() {
        UUID productId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        ProductCreatedEvent event = new ProductCreatedEvent(
                productId,
                companyId,
                null,
                "Café Gourmet",
                "Café de altura",
                new BigDecimal("12.50"),
                true,
                "https://vazzar.com/cafe.png"
        );

        // 1. Ecommerce lo escucha
        ecommerceProductListener.onProductCreated(event);
        ArgumentCaptor<EcommerceProductJpaEntity> ecomCaptor = ArgumentCaptor.forClass(EcommerceProductJpaEntity.class);
        verify(ecommerceProductRepository, times(1)).save(ecomCaptor.capture());
        assertEquals("Café Gourmet", ecomCaptor.getValue().getName());

        // 2. Fair lo escucha y lo guarda porque fair_eligible = true
        when(fairCompanyRepository.existsById(companyId)).thenReturn(false);
        fairProductListener.onProductCreated(event);
        verify(fairCompanyRepository, times(1)).save(any(FairCompanyJpaEntity.class));
        verify(fairProductRepository, times(1)).save(any(FairProductJpaEntity.class));
    }

    @Test
    void shouldNotSyncProductToFairWhenFairEligibleIsFalse() {
        UUID productId = UUID.randomUUID();
        UUID companyId = UUID.randomUUID();
        ProductCreatedEvent event = new ProductCreatedEvent(
                productId,
                companyId,
                null,
                "Producto No Feria",
                "Solo para tienda permanente",
                new BigDecimal("15.00"),
                false, // NOT eligible for fair
                null
        );

        // Fair listener debe ignorarlo
        fairProductListener.onProductCreated(event);
        verify(fairProductRepository, never()).save(any());
    }
}
