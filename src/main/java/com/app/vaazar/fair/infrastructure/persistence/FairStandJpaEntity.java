package com.app.vaazar.fair.infrastructure.persistence;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "stand", schema = "fair")
public class FairStandJpaEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "merchant_email", nullable = false, length = 255)
    private String merchantEmail;

    @Column(name = "merchant_name", nullable = false, length = 255)
    private String merchantName;

    @Column(name = "stand_number", nullable = false, length = 50)
    private String standNumber;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 100)
    private String pavilion;

    @Column(name = "canopy_color", nullable = false, length = 50)
    private String canopyColor;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url", length = 1000)
    private String logoUrl;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "stand", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<FairStandProductJpaEntity> products = new ArrayList<>();

    public FairStandJpaEntity() {
    }

    public FairStandJpaEntity(UUID id, String merchantEmail, String merchantName, String standNumber,
                              String name, String pavilion, String canopyColor, String description,
                              String logoUrl) {
        this.id = id;
        this.merchantEmail = merchantEmail;
        this.merchantName = merchantName;
        this.standNumber = standNumber;
        this.name = name;
        this.pavilion = pavilion;
        this.canopyColor = canopyColor != null ? canopyColor : "#6366f1";
        this.description = description;
        this.logoUrl = logoUrl;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getStandNumber() {
        return standNumber;
    }

    public void setStandNumber(String standNumber) {
        this.standNumber = standNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPavilion() {
        return pavilion;
    }

    public void setPavilion(String pavilion) {
        this.pavilion = pavilion;
    }

    public String getCanopyColor() {
        return canopyColor;
    }

    public void setCanopyColor(String canopyColor) {
        this.canopyColor = canopyColor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<FairStandProductJpaEntity> getProducts() {
        return products;
    }

    public void setProducts(List<FairStandProductJpaEntity> products) {
        this.products = products;
    }

    public void addProduct(FairStandProductJpaEntity product) {
        products.add(product);
        product.setStand(this);
    }

    public void removeProduct(FairStandProductJpaEntity product) {
        products.remove(product);
        product.setStand(null);
    }
}
