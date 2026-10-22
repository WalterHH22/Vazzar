package com.app.vaazar.inventory.infrastructure.web.dto;

import java.util.UUID;

public class CategoryResponse {
    private UUID id;
    private String name;
    private UUID createdBy;
    private String creatorEmail;

    public CategoryResponse() {
    }

    public CategoryResponse(UUID id, String name, UUID createdBy, String creatorEmail) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.creatorEmail = creatorEmail;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }
}
