package com.app.vaazar.inventory.infrastructure.web;

import com.app.vaazar.identity.domain.model.User;
import com.app.vaazar.identity.domain.port.TokenProviderPort;
import com.app.vaazar.identity.domain.port.UserRepositoryPort;
import com.app.vaazar.inventory.application.usecase.CategoryService;
import com.app.vaazar.inventory.domain.model.Category;
import com.app.vaazar.inventory.infrastructure.web.dto.CategoryResponse;
import com.app.vaazar.inventory.infrastructure.web.dto.CreateCategoryRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;
    private final TokenProviderPort tokenProviderPort;
    private final UserRepositoryPort userRepositoryPort;

    public CategoryController(CategoryService categoryService,
                              TokenProviderPort tokenProviderPort,
                              UserRepositoryPort userRepositoryPort) {
        this.categoryService = categoryService;
        this.tokenProviderPort = tokenProviderPort;
        this.userRepositoryPort = userRepositoryPort;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestBody CreateCategoryRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        UUID userId = extractUserId(authHeader);
        String email = extractEmail(authHeader);
        Category created = categoryService.create(request.getName(), userId, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @GetMapping
    public List<CategoryResponse> listAll() {
        return categoryService.listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable UUID id) {
        return categoryService.getById(id)
                .map(c -> ResponseEntity.ok(toResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable UUID id, @RequestBody CreateCategoryRequest request) {
        return categoryService.update(id, request.getName())
                .map(c -> ResponseEntity.ok(toResponse(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable UUID id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        UUID userId = extractUserId(authHeader);
        String role = extractRole(authHeader);

        try {
            boolean deleted = categoryService.delete(id, userId, role);
            if (deleted) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "status", HttpStatus.FORBIDDEN.value(),
                    "error", "Forbidden",
                    "message", e.getMessage()
            ));
        }
    }

    private CategoryResponse toResponse(Category category) {
        String email = category.getCreatedByEmail();
        if (email == null && category.getCreatedBy() != null) {
            email = userRepositoryPort.findById(category.getCreatedBy())
                    .map(User::getEmail)
                    .orElse(null);
        }
        return new CategoryResponse(category.getId(), category.getName(), category.getCreatedBy(), email);
    }

    private String extractEmail(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (tokenProviderPort.validateToken(token)) {
                return tokenProviderPort.extractEmail(token);
            }
        }
        return null;
    }

    private UUID extractUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (tokenProviderPort.validateToken(token)) {
                return tokenProviderPort.extractUserId(token);
            }
        }
        return null;
    }

    private String extractRole(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (tokenProviderPort.validateToken(token)) {
                return tokenProviderPort.extractRole(token);
            }
        }
        return null;
    }
}
