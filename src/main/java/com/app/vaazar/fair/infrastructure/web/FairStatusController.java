package com.app.vaazar.fair.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/fair")
@CrossOrigin(origins = "*")
public class FairStatusController {

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(Map.of(
                "module", "FAIR",
                "schema", "fair",
                "type", "Supporting",
                "status", "ACTIVE",
                "message", "Módulo de Ferias Virtuales (Fair) en línea",
                "timestamp", Instant.now()
        ));
    }
}
