package com.app.vaazar.identity.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/identity")
@CrossOrigin(origins = "*")
public class IdentityStatusController {

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(Map.of(
                "module", "IDENTITY",
                "schema", "identity",
                "type", "Generic/Support",
                "status", "ACTIVE",
                "message", "Módulo de Identidad y Autenticación en línea",
                "timestamp", Instant.now()
        ));
    }
}
