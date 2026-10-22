package com.app.vaazar.inventory.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ProductCreatedEvent {
    private final UUID productId;
    private final UUID companyId;
    private final UUID categoryId;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean fairEligible;
    private final String imageUrl;
    private final Instant occurredOn;

    public ProductCreatedEvent(UUID productId, UUID companyId, UUID categoryId, String name, String description,
                               BigDecimal price, boolean fairEligible, String imageUrl) {
        this.productId = productId;
        this.companyId = companyId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price != null ? price : BigDecimal.ZERO;
        this.fairEligible = fairEligible;
        this.imageUrl = imageUrl;
        this.occurredOn = Instant.now();
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isFairEligible() {
        return fairEligible;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
