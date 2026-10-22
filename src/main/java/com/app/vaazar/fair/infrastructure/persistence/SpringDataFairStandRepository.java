package com.app.vaazar.fair.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataFairStandRepository extends JpaRepository<FairStandJpaEntity, UUID> {
    Optional<FairStandJpaEntity> findByMerchantEmailIgnoreCase(String merchantEmail);
    long count();
}
