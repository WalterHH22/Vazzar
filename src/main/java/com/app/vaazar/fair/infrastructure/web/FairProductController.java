package com.app.vaazar.fair.infrastructure.web;

import com.app.vaazar.fair.infrastructure.persistence.FairProductJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.FairStandJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.FairStandProductJpaEntity;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairProductRepository;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairStandProductRepository;
import com.app.vaazar.fair.infrastructure.persistence.SpringDataFairStandRepository;
import com.app.vaazar.fair.infrastructure.web.dto.AddStandProductRequest;
import com.app.vaazar.fair.infrastructure.web.dto.CreateOrUpdateStandRequest;
import com.app.vaazar.fair.infrastructure.web.dto.FairStandProductDto;
import com.app.vaazar.fair.infrastructure.web.dto.FairStandResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fair")
@CrossOrigin(origins = "*")
public class FairProductController {

    private final SpringDataFairStandRepository standRepository;
    private final SpringDataFairStandProductRepository standProductRepository;
    private final SpringDataFairProductRepository fairProductRepository;

    public FairProductController(SpringDataFairStandRepository standRepository,
                                 SpringDataFairStandProductRepository standProductRepository,
                                 SpringDataFairProductRepository fairProductRepository) {
        this.standRepository = standRepository;
        this.standProductRepository = standProductRepository;
        this.fairProductRepository = fairProductRepository;
    }

    @GetMapping("/products")
    public ResponseEntity<List<FairProductJpaEntity>> listFairCatalog() {
        return ResponseEntity.ok(fairProductRepository.findAll());
    }

    @GetMapping("/stands")
    public ResponseEntity<List<FairStandResponse>> listStands() {
        List<FairStandJpaEntity> stands = standRepository.findAll();
        stands.sort(Comparator.comparing(FairStandJpaEntity::getStandNumber, Comparator.nullsLast(String::compareTo)));

        List<FairStandResponse> responses = stands.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/stands/my-stand")
    public ResponseEntity<?> getMyStand(@RequestParam("email") String email) {
        Optional<FairStandJpaEntity> standOpt = standRepository.findByMerchantEmailIgnoreCase(email);
        if (standOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(mapToResponse(standOpt.get()));
    }

    @PostMapping("/stands")
    @Transactional
    public ResponseEntity<FairStandResponse> createStand(@RequestBody CreateOrUpdateStandRequest request) {
        if (request.getMerchantEmail() == null || request.getMerchantEmail().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Si ya existe stand para ese email, retornamos el existente
        Optional<FairStandJpaEntity> existing = standRepository.findByMerchantEmailIgnoreCase(request.getMerchantEmail());
        if (existing.isPresent()) {
            return ResponseEntity.ok(mapToResponse(existing.get()));
        }

        String standNum = request.getStandNumber();
        if (standNum == null || standNum.isBlank()) {
            long count = standRepository.count() + 1;
            standNum = String.format("STAND #%02d", count);
        }

        FairStandJpaEntity stand = new FairStandJpaEntity(
                UUID.randomUUID(),
                request.getMerchantEmail().trim().toLowerCase(),
                request.getMerchantName() != null ? request.getMerchantName().trim() : "Comerciante Vazzar",
                standNum,
                request.getName() != null ? request.getName().trim() : "Mi Puesto Oficial",
                request.getPavilion() != null ? request.getPavilion().trim() : "General",
                request.getCanopyColor() != null ? request.getCanopyColor().trim() : "#6366f1",
                request.getDescription(),
                request.getLogoUrl()
        );

        FairStandJpaEntity saved = standRepository.save(stand);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(saved));
    }

    @PutMapping("/stands/{id}")
    @Transactional
    public ResponseEntity<FairStandResponse> updateStand(@PathVariable("id") UUID id,
                                                         @RequestBody CreateOrUpdateStandRequest request) {
        Optional<FairStandJpaEntity> opt = standRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FairStandJpaEntity stand = opt.get();
        if (request.getName() != null && !request.getName().isBlank()) {
            stand.setName(request.getName().trim());
        }
        if (request.getMerchantName() != null && !request.getMerchantName().isBlank()) {
            stand.setMerchantName(request.getMerchantName().trim());
        }
        if (request.getPavilion() != null && !request.getPavilion().isBlank()) {
            stand.setPavilion(request.getPavilion().trim());
        }
        if (request.getCanopyColor() != null && !request.getCanopyColor().isBlank()) {
            stand.setCanopyColor(request.getCanopyColor().trim());
        }
        if (request.getDescription() != null) {
            stand.setDescription(request.getDescription().trim());
        }
        if (request.getLogoUrl() != null) {
            stand.setLogoUrl(request.getLogoUrl().trim());
        }
        stand.setUpdatedAt(Instant.now());

        FairStandJpaEntity updated = standRepository.save(stand);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @PostMapping("/stands/{id}/products")
    @Transactional
    public ResponseEntity<FairStandProductDto> addProductToStand(@PathVariable("id") UUID id,
                                                                 @RequestBody AddStandProductRequest request) {
        Optional<FairStandJpaEntity> opt = standRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FairStandJpaEntity stand = opt.get();
        FairStandProductJpaEntity prod = new FairStandProductJpaEntity(
                UUID.randomUUID(),
                stand,
                request.getProductId(),
                request.getName() != null ? request.getName().trim() : "Producto de Feria",
                request.getDescription(),
                request.getPrice(),
                request.getStock() != null ? request.getStock() : 50,
                request.getImageUrl()
        );

        stand.addProduct(prod);
        standRepository.save(stand);

        return ResponseEntity.status(HttpStatus.CREATED).body(new FairStandProductDto(
                prod.getId(),
                prod.getProductId(),
                prod.getName(),
                prod.getDescription(),
                prod.getPrice(),
                prod.getStock(),
                prod.getImageUrl()
        ));
    }

    @DeleteMapping("/stands/{id}/products/{productId}")
    @Transactional
    public ResponseEntity<?> removeProductFromStand(@PathVariable("id") UUID id,
                                                    @PathVariable("productId") UUID productId) {
        Optional<FairStandJpaEntity> opt = standRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FairStandJpaEntity stand = opt.get();
        stand.getProducts().removeIf(p -> p.getId().equals(productId));
        standRepository.save(stand);

        return ResponseEntity.noContent().build();
    }

    private FairStandResponse mapToResponse(FairStandJpaEntity stand) {
        List<FairStandProductDto> productDtos = stand.getProducts().stream()
                .map(p -> new FairStandProductDto(
                        p.getId(),
                        p.getProductId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock(),
                        p.getImageUrl()
                ))
                .collect(Collectors.toList());

        return new FairStandResponse(
                stand.getId(),
                stand.getStandNumber(),
                stand.getName(),
                stand.getMerchantName(),
                stand.getMerchantEmail(),
                stand.getPavilion(),
                stand.getCanopyColor(),
                stand.getDescription(),
                stand.getLogoUrl(),
                productDtos
        );
    }
}
