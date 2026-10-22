package com.app.vaazar.inventory.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataInventoryProductRepository extends JpaRepository<InventoryProductJpaEntity, UUID> {
}
