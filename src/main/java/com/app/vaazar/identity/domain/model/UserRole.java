package com.app.vaazar.identity.domain.model;

public enum UserRole {
    ADMIN("admin"),
    SELLER("seller"),
    BUYER("buyer");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRole fromString(String value) {
        if (value == null) return null;
        for (UserRole role : values()) {
            if (role.name().equalsIgnoreCase(value) || role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol inválido: " + value + ". Roles permitidos: admin, seller, buyer");
    }
}
