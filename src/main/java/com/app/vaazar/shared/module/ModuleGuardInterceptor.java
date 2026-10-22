package com.app.vaazar.shared.module;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.Instant;

@Component
public class ModuleGuardInterceptor implements HandlerInterceptor {

    private final ModuleRegistry moduleRegistry;

    public ModuleGuardInterceptor(ModuleRegistry moduleRegistry) {
        this.moduleRegistry = moduleRegistry;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Admin endpoints and static files bypass the guard
        if (uri.startsWith("/api/admin") || uri.startsWith("/admin")) {
            return true;
        }

        ModuleId module = ModuleId.fromPath(uri);
        if (module != null && !moduleRegistry.isModuleEnabled(module)) {
            sendDisabledResponse(response, module, uri);
            return false;
        }

        return true;
    }

    private void sendDisabledResponse(HttpServletResponse response, ModuleId module, String uri) throws IOException {
        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String json = String.format(
                "{\"timestamp\":\"%s\",\"status\":503,\"error\":\"Service Unavailable\",\"module\":\"%s\",\"schema\":\"%s\",\"message\":\"El módulo '%s' (%s) está actualmente APAGADO (DISABLED).\",\"requestedUri\":\"%s\",\"hint\":\"Puedes encender este módulo desde el panel de control (/admin/modules) o vía PATCH /api/admin/modules/%s/toggle\"}",
                Instant.now(),
                module.name(),
                module.getSchema(),
                module.getKey(),
                module.getDisplayName(),
                uri,
                module.name()
        );

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
