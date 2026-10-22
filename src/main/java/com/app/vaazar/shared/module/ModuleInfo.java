package com.app.vaazar.shared.module;

import java.util.Map;

public class ModuleInfo {
    private String id;
    private String key;
    private String displayName;
    private String schema;
    private String contextType;
    private String basePath;
    private boolean enabled;
    private String testEndpoint;
    private Map<String, Object> metadata;

    public ModuleInfo() {
    }

    public ModuleInfo(ModuleId module, boolean enabled, String testEndpoint, Map<String, Object> metadata) {
        this.id = module.name();
        this.key = module.getKey();
        this.displayName = module.getDisplayName();
        this.schema = module.getSchema();
        this.contextType = module.getContextType();
        this.basePath = module.getBasePath();
        this.enabled = enabled;
        this.testEndpoint = testEndpoint;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTestEndpoint() {
        return testEndpoint;
    }

    public void setTestEndpoint(String testEndpoint) {
        this.testEndpoint = testEndpoint;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
