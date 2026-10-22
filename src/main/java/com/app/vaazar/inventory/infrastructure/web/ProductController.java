package com.app.vaazar.inventory.infrastructure.web;

import com.app.vaazar.inventory.application.usecase.ProductService;
import com.app.vaazar.inventory.infrastructure.persistence.InventoryProductJpaEntity;
import com.app.vaazar.inventory.infrastructure.web.dto.CreateProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<InventoryProductJpaEntity> create(@Valid @RequestBody CreateProductRequest request) {
        InventoryProductJpaEntity created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<InventoryProductJpaEntity>> listAll() {
        return ResponseEntity.ok(productService.listAll());
    }
}
