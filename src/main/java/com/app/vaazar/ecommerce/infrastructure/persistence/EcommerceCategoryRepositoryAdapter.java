package com.app.vaazar.ecommerce.infrastructure.persistence;

import com.app.vaazar.ecommerce.domain.model.EcommerceCategory;
import com.app.vaazar.ecommerce.domain.port.EcommerceCategoryRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class EcommerceCategoryRepositoryAdapter implements EcommerceCategoryRepositoryPort {

    private final SpringDataEcommerceCategoryRepository jpaRepository;

    public EcommerceCategoryRepositoryAdapter(SpringDataEcommerceCategoryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public EcommerceCategory save(EcommerceCategory category) {
        EcommerceCategoryJpaEntity entity = new EcommerceCategoryJpaEntity(category.getId(), category.getName());
        EcommerceCategoryJpaEntity saved = jpaRepository.save(entity);
        return new EcommerceCategory(saved.getId(), saved.getName());
    }

    @Override
    public Optional<EcommerceCategory> findById(UUID id) {
        return jpaRepository.findById(id).map(e -> new EcommerceCategory(e.getId(), e.getName()));
    }

    @Override
    public List<EcommerceCategory> findAll() {
        return jpaRepository.findAll().stream()
                .map(e -> new EcommerceCategory(e.getId(), e.getName()))
                .toList();
    }
}
