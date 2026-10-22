package com.app.vaazar.ecommerce.domain.model;

import java.util.UUID;

public class EcommerceCategory {
    private final UUID id;
    private String name;

    public EcommerceCategory(UUID id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
        }
        this.id = id;
        this.name = name.trim();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.name = name.trim();
    }
}
