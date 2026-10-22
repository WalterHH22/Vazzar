package com.app.vaazar.ecommerce.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/ecommerce")
@CrossOrigin(origins = "*")
public class EcommerceStatusController {

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(Map.of(
                "module", "ECOMMERCE",
                "schema", "ecommerce",
                "type", "Supporting",
                "status", "ACTIVE",
                "message", "Módulo de Tienda Online (Ecommerce) en línea",
                "timestamp", Instant.now()
        ));
    }
}
