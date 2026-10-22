package com.app.vaazar.ecommerce.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataEcommerceCategoryRepository extends JpaRepository<EcommerceCategoryJpaEntity, UUID> {
}
