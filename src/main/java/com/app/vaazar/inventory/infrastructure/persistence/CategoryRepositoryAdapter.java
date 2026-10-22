package com.app.vaazar.inventory.infrastructure.persistence;

import com.app.vaazar.inventory.domain.model.Category;
import com.app.vaazar.inventory.domain.port.CategoryRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final SpringDataCategoryRepository jpaRepository;

    public CategoryRepositoryAdapter(SpringDataCategoryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = new CategoryJpaEntity(category.getId(), category.getName());
        CategoryJpaEntity saved = jpaRepository.save(entity);
        return new Category(saved.getId(), saved.getName());
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> new Category(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(entity -> new Category(entity.getId(), entity.getName()));
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}