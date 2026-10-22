package com.app.vaazar.inventory.domain.event;

import java.time.Instant;
import java.util.UUID;

public class ProductDeletedEvent {
    private final UUID productId;
    private final Instant occurredOn;

    public ProductDeletedEvent(UUID productId) {
        this.productId = productId;
        this.occurredOn = Instant.now();
    }

    public UUID getProductId() {
        return productId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
