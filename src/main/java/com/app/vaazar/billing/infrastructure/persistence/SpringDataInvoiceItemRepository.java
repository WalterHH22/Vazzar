package com.app.vaazar.billing.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataInvoiceItemRepository extends JpaRepository<InvoiceItemJpaEntity, UUID> {
}
