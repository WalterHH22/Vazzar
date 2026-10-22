package com.app.vaazar.shared.module;

public enum ModuleId {
    IDENTITY("identity", "Identidad y Autenticación", "identity", "Generic/Support", "/api/identity"),
    INVENTORY("inventory", "Inventario y Catálogo (Core)", "inventory", "Core", "/api/inventory"),
    BILLING("billing", "Facturación", "billing", "Generic/Support", "/api/billing"),
    ECOMMERCE("ecommerce", "Tienda Online (Ecommerce)", "ecommerce", "Supporting", "/api/ecommerce"),
    FAIR("fair", "Ferias Virtuales (Fair)", "fair", "Supporting", "/api/fair");

    private final String key;
    private final String displayName;
    private final String schema;
    private final String contextType;
    private final String basePath;

    ModuleId(String key, String displayName, String schema, String contextType, String basePath) {
        this.key = key;
        this.displayName = displayName;
        this.schema = schema;
        this.contextType = contextType;
        this.basePath = basePath;
    }

    public String getKey() {
        return key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSchema() {
        return schema;
    }

    public String getContextType() {
        return contextType;
    }

    public String getBasePath() {
        return basePath;
    }

    public static ModuleId fromPath(String path) {
        if (path == null) return null;
        for (ModuleId module : values()) {
            if (path.startsWith(module.getBasePath())) {
                return module;
            }
        }
        return null;
    }

    public static ModuleId fromString(String text) {
        if (text == null) return null;
        for (ModuleId module : values()) {
            if (module.name().equalsIgnoreCase(text) || module.getKey().equalsIgnoreCase(text)) {
                return module;
            }
        }
        return null;
    }
}
