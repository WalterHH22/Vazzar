package com.app.vaazar.ecommerce.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataEcommerceProductRepository extends JpaRepository<EcommerceProductJpaEntity, UUID> {
    List<EcommerceProductJpaEntity> findByAvailableTrue();
}
