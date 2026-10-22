package com.app.vaazar.shared.module;

import jakarta.annotation.PostConstruct;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ModuleRegistry {

    private final Environment environment;
    private final Map<ModuleId, Boolean> moduleStates = new ConcurrentHashMap<>();

    public ModuleRegistry(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        for (ModuleId module : ModuleId.values()) {
            String propertyKey = "modules." + module.getKey() + ".enabled";
            // Default: identity and inventory enabled, others can be configured via application.properties
            boolean defaultValue = (module == ModuleId.IDENTITY || module == ModuleId.INVENTORY);
            Boolean configured = environment.getProperty(propertyKey, Boolean.class, defaultValue);
            moduleStates.put(module, configured);
        }
    }

    public boolean isModuleEnabled(ModuleId module) {
        return moduleStates.getOrDefault(module, false);
    }

    public boolean isModuleEnabled(String path) {
        ModuleId module = ModuleId.fromPath(path);
        if (module == null) {
            return true; // Paths outside bounded contexts (like /api/admin) are not blocked
        }
        return isModuleEnabled(module);
    }

    public boolean setModuleStatus(ModuleId module, boolean enabled) {
        moduleStates.put(module, enabled);
        return enabled;
    }

    public boolean toggleModule(ModuleId module) {
        boolean current = isModuleEnabled(module);
        boolean next = !current;
        moduleStates.put(module, next);
        return next;
    }

    public List<ModuleInfo> getAllModules() {
        List<ModuleInfo> list = new ArrayList<>();
        for (ModuleId module : ModuleId.values()) {
            boolean enabled = isModuleEnabled(module);
            String testEndpoint = switch (module) {
                case INVENTORY -> "/api/inventory/categories";
                case IDENTITY -> "/api/identity/status";
                case BILLING -> "/api/billing/status";
                case ECOMMERCE -> "/api/ecommerce/status";
                case FAIR -> "/api/fair/status";
            };

            Map<String, Object> metadata = new LinkedHashMap<>();
            metadata.put("tables", switch (module) {
                case IDENTITY -> List.of("user");
                case INVENTORY -> List.of("company", "supplier", "category", "subcategory", "product");
                case BILLING -> List.of("billing_customer", "invoice", "invoice_item");
                case ECOMMERCE -> List.of("category", "product", "cart", "cart_item");
                case FAIR -> List.of("fair", "company", "product", "fair_company", "fair_product");
            });

            list.add(new ModuleInfo(module, enabled, testEndpoint, metadata));
        }
        return list;
    }

    public Optional<ModuleInfo> getModule(ModuleId module) {
        return getAllModules().stream().filter(m -> m.getId().equalsIgnoreCase(module.name())).findFirst();
    }
}
