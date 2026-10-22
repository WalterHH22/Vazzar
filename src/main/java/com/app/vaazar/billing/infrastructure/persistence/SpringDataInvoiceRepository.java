package com.app.vaazar.billing.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataInvoiceRepository extends JpaRepository<InvoiceJpaEntity, UUID> {
    List<InvoiceJpaEntity> findAllByOrderByIssuedAtDesc();
}
