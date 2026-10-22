package com.app.vaazar.fair.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "company", schema = "fair")
public class FairCompanyJpaEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "logo_url", length = 1000)
    private String logoUrl;

    @Column(length = 255)
    private String contact;

    public FairCompanyJpaEntity() {
    }

    public FairCompanyJpaEntity(UUID id, String name, String logoUrl, String contact) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.contact = contact;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
