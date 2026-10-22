package com.app.vaazar.inventory.domain.event;

import java.time.Instant;
import java.util.UUID;

public class CategoryDeletedEvent {
    private final UUID categoryId;
    private final Instant occurredOn;

    public CategoryDeletedEvent(UUID categoryId) {
        this.categoryId = categoryId;
        this.occurredOn = Instant.now();
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
