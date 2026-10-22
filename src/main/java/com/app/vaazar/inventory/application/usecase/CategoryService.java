package com.app.vaazar.inventory.application.usecase;

import com.app.vaazar.inventory.domain.event.CategoryCreatedEvent;
import com.app.vaazar.inventory.domain.event.CategoryUpdatedEvent;
import com.app.vaazar.inventory.domain.model.Category;
import com.app.vaazar.inventory.domain.port.CategoryRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepositoryPort repositoryPort;
    private final ApplicationEventPublisher eventPublisher;

    public CategoryService(CategoryRepositoryPort repositoryPort, ApplicationEventPublisher eventPublisher) {
        this.repositoryPort = repositoryPort;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Category create(String name) {
        Category category = new Category(UUID.randomUUID(), name);
        Category saved = repositoryPort.save(category);
        eventPublisher.publishEvent(new CategoryCreatedEvent(saved.getId(), saved.getName()));
        return saved;
    }

    public List<Category> listAll() {
        return repositoryPort.findAll();
    }

    public Optional<Category> getById(UUID id) {
        return repositoryPort.findById(id);
    }

    @Transactional
    public Optional<Category> update(UUID id, String newName) {
        return repositoryPort.findById(id).map(existing -> {
            existing.updateName(newName);
            Category updated = repositoryPort.save(existing);
            eventPublisher.publishEvent(new CategoryUpdatedEvent(updated.getId(), updated.getName()));
            return updated;
        });
    }

    @Transactional
    public boolean delete(UUID id) {
        if (repositoryPort.findById(id).isPresent()) {
            repositoryPort.deleteById(id);
            return true;
        }
        return false;
    }
}
