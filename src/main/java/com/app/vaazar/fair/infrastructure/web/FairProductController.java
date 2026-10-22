package com.app.vaazar.fair.infrastructure.web;

import com.app.vaazar.fair.infrastructure.persistence.FairProductJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fair/products")
@CrossOrigin(origins = "*")
public class FairProductController {

    private final SpringDataFairProductRepository productRepository;

    public FairProductController(SpringDataFairProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<FairProductJpaEntity>> listFairCatalog() {
        return ResponseEntity.ok(productRepository.findAll());
    }
}
