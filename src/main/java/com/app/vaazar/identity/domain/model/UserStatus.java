package com.app.vaazar.identity.domain.model;

public enum UserStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserStatus fromString(String value) {
        if (value == null) return ACTIVE;
        for (UserStatus status : values()) {
            if (status.name().equalsIgnoreCase(value) || status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado inválido: " + value + ". Estados permitidos: active, inactive");
    }
}
