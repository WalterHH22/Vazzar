package com.app.vaazar.billing.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingStatusController {

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return ResponseEntity.ok(Map.of(
                "module", "BILLING",
                "schema", "billing",
                "type", "Generic/Support",
                "status", "ACTIVE",
                "message", "Módulo de Facturación en línea",
                "timestamp", Instant.now()
        ));
    }
}
