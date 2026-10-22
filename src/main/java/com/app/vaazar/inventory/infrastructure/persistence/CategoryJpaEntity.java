package com.app.vaazar.inventory.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "category", schema = "inventory")
public class CategoryJpaEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_by_email")
    private String createdByEmail;

    public CategoryJpaEntity() {
    }

    public CategoryJpaEntity(UUID id, String name) {
        this(id, name, null, null);
    }

    public CategoryJpaEntity(UUID id, String name, UUID createdBy) {
        this(id, name, createdBy, null);
    }

    public CategoryJpaEntity(UUID id, String name, UUID createdBy, String createdByEmail) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.createdByEmail = createdByEmail;
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

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }
}
