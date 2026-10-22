package com.app.vaazar.billing.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoice", schema = "billing")
public class InvoiceJpaEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "source_channel", nullable = false, length = 20)
    private String sourceChannel = "ecommerce";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "billing_customer_id", nullable = false)
    private BillingCustomerJpaEntity customer;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, length = 20)
    private String status = "issued";

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt = Instant.now();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<InvoiceItemJpaEntity> items = new ArrayList<>();

    public InvoiceJpaEntity() {
    }

    public InvoiceJpaEntity(UUID id, UUID orderId, String sourceChannel, BillingCustomerJpaEntity customer,
                            BigDecimal total, String status) {
        this.id = id;
        this.orderId = orderId;
        this.sourceChannel = sourceChannel != null ? sourceChannel : "ecommerce";
        this.customer = customer;
        this.total = total;
        this.status = status != null ? status : "issued";
        this.issuedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getSourceChannel() {
        return sourceChannel;
    }

    public void setSourceChannel(String sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    public BillingCustomerJpaEntity getCustomer() {
        return customer;
    }

    public void setCustomer(BillingCustomerJpaEntity customer) {
        this.customer = customer;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Instant issuedAt) {
        this.issuedAt = issuedAt;
    }

    public List<InvoiceItemJpaEntity> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemJpaEntity> items) {
        this.items = items;
    }

    public void addItem(InvoiceItemJpaEntity item) {
        items.add(item);
        item.setInvoice(this);
    }
}
