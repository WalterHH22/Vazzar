package com.app.vaazar.inventory.application.usecase;

import com.app.vaazar.inventory.domain.event.CategoryCreatedEvent;
import com.app.vaazar.inventory.domain.event.CategoryDeletedEvent;
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
    public Category create(String name, UUID createdBy, String createdByEmail) {
        Category category = new Category(UUID.randomUUID(), name, createdBy, createdByEmail);
        Category saved = repositoryPort.save(category);
        eventPublisher.publishEvent(new CategoryCreatedEvent(saved.getId(), saved.getName()));
        return saved;
    }

    @Transactional
    public Category create(String name, UUID createdBy) {
        return create(name, createdBy, null);
    }

    @Transactional
    public Category create(String name) {
        return create(name, null, null);
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
    public boolean delete(UUID id, UUID requestedByUserId, String requestedByRole) {
        Optional<Category> opt = repositoryPort.findById(id);
        if (opt.isEmpty()) {
            return false;
        }

        Category category = opt.get();
        boolean isAdmin = "admin".equalsIgnoreCase(requestedByRole);

        // Si la categoría tiene un vendedor asignado como creador:
        if (category.getCreatedBy() != null) {
            boolean isCreator = requestedByUserId != null && category.getCreatedBy().equals(requestedByUserId);
            if (!isCreator && !isAdmin) {
                throw new SecurityException("No tienes permisos para eliminar una categoría creada por otro vendedor.");
            }
        } else {
            // Categoría global inicial (sin creador): solo un administrador puede eliminarla
            if (!isAdmin) {
                throw new SecurityException("No tienes permisos para eliminar una categoría global del sistema.");
            }
        }

        repositoryPort.deleteById(id);
        eventPublisher.publishEvent(new CategoryDeletedEvent(id));
        return true;
    }

    @Transactional
    public boolean delete(UUID id) {
        return delete(id, null, "admin");
    }
}
