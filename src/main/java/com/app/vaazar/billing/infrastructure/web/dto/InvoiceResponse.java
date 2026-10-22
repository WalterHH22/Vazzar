package com.app.vaazar.billing.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class InvoiceResponse {
    private UUID id;
    private UUID orderId;
    private String invoiceNumber;
    private String sourceChannel;
    private UUID customerId;
    private String customerName;
    private String taxId;
    private BigDecimal total;
    private String status;
    private Instant issuedAt;
    private String paymentMethod;
    private List<InvoiceItemResponse> items;

    public InvoiceResponse() {
    }

    public InvoiceResponse(UUID id, UUID orderId, String invoiceNumber, String sourceChannel, UUID customerId,
                           String customerName, String taxId, BigDecimal total, String status, Instant issuedAt,
                           String paymentMethod, List<InvoiceItemResponse> items) {
        this.id = id;
        this.orderId = orderId;
        this.invoiceNumber = invoiceNumber;
        this.sourceChannel = sourceChannel;
        this.customerId = customerId;
        this.customerName = customerName;
        this.taxId = taxId;
        this.total = total;
        this.status = status;
        this.issuedAt = issuedAt;
        this.paymentMethod = paymentMethod;
        this.items = items;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSourceChannel() {
        return sourceChannel;
    }

    public void setSourceChannel(String sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<InvoiceItemResponse> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemResponse> items) {
        this.items = items;
    }
}
