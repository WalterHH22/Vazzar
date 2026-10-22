package com.app.vaazar.inventory.domain.model;

import java.util.UUID;

public class Category {
    private final UUID id;
    private String name;
    private final UUID createdBy;
    private final String createdByEmail;

    public Category(UUID id, String name) {
        this(id, name, null, null);
    }

    public Category(UUID id, String name, UUID createdBy) {
        this(id, name, createdBy, null);
    }

    public Category(UUID id, String name, UUID createdBy, String createdByEmail) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name.trim();
        this.createdBy = createdBy;
        this.createdByEmail = createdByEmail;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");
        }
        this.name = newName.trim();
    }
}
