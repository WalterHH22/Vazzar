package com.app.vaazar.fair.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataFairProductRepository extends JpaRepository<FairProductJpaEntity, UUID> {
}
