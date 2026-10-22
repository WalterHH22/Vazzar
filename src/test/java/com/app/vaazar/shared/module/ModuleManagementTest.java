package com.app.vaazar.shared.module;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class ModuleManagementTest {

    private ModuleRegistry registry;
    private ModuleGuardInterceptor interceptor;

    @BeforeEach
    void setUp() {
        MockEnvironment env = new MockEnvironment();
        env.setProperty("modules.identity.enabled", "true");
        env.setProperty("modules.inventory.enabled", "true");
        env.setProperty("modules.billing.enabled", "false");
        env.setProperty("modules.ecommerce.enabled", "false");
        env.setProperty("modules.fair.enabled", "false");

        registry = new ModuleRegistry(env);
        registry.init();
        interceptor = new ModuleGuardInterceptor(registry);
    }

    @Test
    void shouldAllowRequestsToEnabledModules() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/inventory/categories");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
        assertEquals(200, response.getStatus());
    }

    @Test
    void shouldBlockRequestsToDisabledModulesWith503() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/billing/status");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        assertEquals(503, response.getStatus());
        assertTrue(response.getContentAsString().contains("APAGADO"));
    }

    @Test
    void shouldDynamicallyToggleModuleState() throws Exception {
        // Initially billing is disabled
        assertFalse(registry.isModuleEnabled(ModuleId.BILLING));

        // Toggle it ON
        boolean newState = registry.toggleModule(ModuleId.BILLING);
        assertTrue(newState);
        assertTrue(registry.isModuleEnabled(ModuleId.BILLING));

        // Now request to billing should be allowed
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/billing/status");
        MockHttpServletResponse response = new MockHttpServletResponse();
        boolean allowed = interceptor.preHandle(request, response, new Object());
        assertTrue(allowed);

        // Toggle it back OFF
        newState = registry.toggleModule(ModuleId.BILLING);
        assertFalse(newState);
        assertFalse(registry.isModuleEnabled(ModuleId.BILLING));
    }

    @Test
    void shouldAlwaysAllowAdminAndStaticEndpoints() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/admin/modules");
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
    }
}
