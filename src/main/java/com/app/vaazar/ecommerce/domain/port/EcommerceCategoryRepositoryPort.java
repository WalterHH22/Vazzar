package com.app.vaazar.ecommerce.domain.port;

import com.app.vaazar.ecommerce.domain.model.EcommerceCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EcommerceCategoryRepositoryPort {
    EcommerceCategory save(EcommerceCategory category);
    Optional<EcommerceCategory> findById(UUID id);
    List<EcommerceCategory> findAll();
}
