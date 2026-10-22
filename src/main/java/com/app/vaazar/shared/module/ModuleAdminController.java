package com.app.vaazar.shared.module;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/modules")
@CrossOrigin(origins = "*")
public class ModuleAdminController {

    private final ModuleRegistry moduleRegistry;

    public ModuleAdminController(ModuleRegistry moduleRegistry) {
        this.moduleRegistry = moduleRegistry;
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
    public ResponseEntity<?> toggleModule(@PathVariable String moduleId) {
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
    public ResponseEntity<?> setModuleStatus(@PathVariable String moduleId, @RequestBody Map<String, Boolean> body) {
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
    public ResponseEntity<?> enableAll() {
        for (ModuleId module : ModuleId.values()) {
            moduleRegistry.setModuleStatus(module, true);
        }
        return ResponseEntity.ok(Map.of("message", "Todos los módulos han sido ENCENDIDOS", "modules", moduleRegistry.getAllModules()));
    }

    @PostMapping("/bulk/disable-all")
    public ResponseEntity<?> disableAll() {
        for (ModuleId module : ModuleId.values()) {
            moduleRegistry.setModuleStatus(module, false);
        }
        return ResponseEntity.ok(Map.of("message", "Todos los módulos han sido APAGADOS", "modules", moduleRegistry.getAllModules()));
    }
}
