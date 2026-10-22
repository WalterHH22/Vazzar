package com.app.vaazar.ecommerce.infrastructure.web;

import com.app.vaazar.ecommerce.domain.model.EcommerceCategory;
import com.app.vaazar.ecommerce.domain.port.EcommerceCategoryRepositoryPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/categories")
@CrossOrigin(origins = "*")
public class EcommerceCategoryController {

    private final EcommerceCategoryRepositoryPort repositoryPort;

    public EcommerceCategoryController(EcommerceCategoryRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @GetMapping
    public ResponseEntity<List<EcommerceCategory>> listAll() {
        return ResponseEntity.ok(repositoryPort.findAll());
    }
}
