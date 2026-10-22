package com.app.vaazar.shared.module;

import com.app.vaazar.identity.domain.port.TokenProviderPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/modules")
@CrossOrigin(origins = "*")
public class ModuleAdminController {

    private final ModuleRegistry moduleRegistry;
    private final TokenProviderPort tokenProviderPort;

    public ModuleAdminController(ModuleRegistry moduleRegistry, TokenProviderPort tokenProviderPort) {
        this.moduleRegistry = moduleRegistry;
        this.tokenProviderPort = tokenProviderPort;
    }

    @GetMapping
    public ResponseEntity<List<ModuleInfo>> listModules() {
        return ResponseEntity.ok(moduleRegistry.getAllModules());
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<?> getModule(@PathVariable String moduleId) {
        ModuleId module = ModuleId.fromString(moduleId);
        if (module == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Módulo no encontrado: " + moduleId));
        }
        return moduleRegistry.getModule(module)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{moduleId}/toggle")
    public ResponseEntity<?> toggleModule(
            @PathVariable String moduleId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Si se envía token, validar que sea rol 'admin'
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (tokenProviderPort.validateToken(token)) {
                String role = tokenProviderPort.extractRole(token);
                if (!"admin".equalsIgnoreCase(role)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                            "status", HttpStatus.FORBIDDEN.value(),
                            "error", "Forbidden",
                            "message", "Acceso denegado: Solo los administradores pueden encender o apagar módulos. Tu rol actual es: " + role
                    ));
                }
            }
        }

        ModuleId module = ModuleId.fromString(moduleId);
        if (module == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Módulo no encontrado: " + moduleId));
        }
        boolean newState = moduleRegistry.toggleModule(module);
        return ResponseEntity.ok(Map.of(
                "module", module.name(),
                "enabled", newState,
                "message", "Módulo '" + module.getDisplayName() + "' ahora está " + (newState ? "ENCENDIDO (ENABLED)" : "APAGADO (DISABLED)")
        ));
    }

    @PutMapping("/{moduleId}/status")
    public ResponseEntity<?> setModuleStatus(
            @PathVariable String moduleId,
            @RequestBody Map<String, Boolean> body,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (tokenProviderPort.validateToken(token)) {
                String role = tokenProviderPort.extractRole(token);
                if (!"admin".equalsIgnoreCase(role)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                            "status", HttpStatus.FORBIDDEN.value(),
                            "error", "Forbidden",
                            "message", "Acceso denegado: Solo los administradores pueden cambiar el estado de los módulos."
                    ));
                }
            }
        }

        ModuleId module = ModuleId.fromString(moduleId);
        if (module == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Módulo no encontrado: " + moduleId));
        }
        Boolean targetState = body.get("enabled");
        if (targetState == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Campo 'enabled' (booleano) requerido en el cuerpo"));
        }
        moduleRegistry.setModuleStatus(module, targetState);
        return ResponseEntity.ok(Map.of(
                "module", module.name(),
                "enabled", targetState,
                "message", "Estado del módulo '" + module.getDisplayName() + "' actualizado a " + (targetState ? "ENCENDIDO" : "APAGADO")
        ));
    }

    @PostMapping("/bulk/enable-all")
    public ResponseEntity<?> enableAll(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        for (ModuleId module : ModuleId.values()) {
            moduleRegistry.setModuleStatus(module, true);
        }
        return ResponseEntity.ok(Map.of("message", "Todos los módulos han sido ENCENDIDOS", "modules", moduleRegistry.getAllModules()));
    }

    @PostMapping("/bulk/disable-all")
    public ResponseEntity<?> disableAll(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        for (ModuleId module : ModuleId.values()) {
            moduleRegistry.setModuleStatus(module, false);
        }
        return ResponseEntity.ok(Map.of("message", "Todos los módulos han sido APAGADOS", "modules", moduleRegistry.getAllModules()));
    }
}
