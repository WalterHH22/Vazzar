package com.app.vaazar.inventory.domain.port;

import com.app.vaazar.inventory.domain.model.Category;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryPort {
    Category save(Category category);
    List<Category> findAll();
    Optional<Category> findById(UUID id);
    void deleteById(UUID id);
}