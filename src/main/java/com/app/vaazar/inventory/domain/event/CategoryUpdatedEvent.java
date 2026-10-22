package com.app.vaazar.inventory.domain.event;

import java.time.Instant;
import java.util.UUID;

public class CategoryUpdatedEvent {
    private final UUID categoryId;
    private final String name;
    private final Instant occurredOn;

    public CategoryUpdatedEvent(UUID categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
        this.occurredOn = Instant.now();
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }
}
