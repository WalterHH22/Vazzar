package com.app.vaazar.identity.domain.model;

import java.time.Instant;
import java.util.UUID;

public class User {
    private final UUID id;
    private String email;
    private String passwordHash;
    private UserRole role;
    private UserStatus status;
    private final Instant createdAt;

    public User(UUID id, String email, String passwordHash, UserRole role, UserStatus status, Instant createdAt) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("El password hash no puede estar vacío");
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.email = email.trim().toLowerCase();
        this.passwordHash = passwordHash;
        this.role = role != null ? role : UserRole.BUYER;
        this.status = status != null ? status : UserStatus.ACTIVE;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public void updatePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");
        }
        this.passwordHash = newPasswordHash;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }
}
