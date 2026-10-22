package com.app.vaazar.inventory.domain.model;

import java.util.UUID;

public class Category {
    private final UUID id;
    private String name;

    public Category(UUID id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name.trim();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");
        }
        this.name = newName.trim();
    }
}