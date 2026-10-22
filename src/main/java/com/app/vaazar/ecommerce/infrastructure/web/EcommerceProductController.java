package com.app.vaazar.ecommerce.infrastructure.web;

import com.app.vaazar.ecommerce.infrastructure.persistence.EcommerceProductJpaEntity;
import com.app.vaazar.ecommerce.infrastructure.persistence.SpringDataEcommerceProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ecommerce/products")
@CrossOrigin(origins = "*")
public class EcommerceProductController {

    private final SpringDataEcommerceProductRepository productRepository;

    public EcommerceProductController(SpringDataEcommerceProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<EcommerceProductJpaEntity>> listCatalog() {
        return ResponseEntity.ok(productRepository.findByAvailableTrue());
    }
}
