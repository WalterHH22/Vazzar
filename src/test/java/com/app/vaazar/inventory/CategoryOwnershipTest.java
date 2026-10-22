package com.app.vaazar.inventory;

import com.app.vaazar.inventory.application.usecase.CategoryService;
import com.app.vaazar.inventory.domain.model.Category;
import com.app.vaazar.inventory.domain.port.CategoryRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryOwnershipTest {

    private CategoryRepositoryPort repositoryPort;
    private ApplicationEventPublisher eventPublisher;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        repositoryPort = mock(CategoryRepositoryPort.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        categoryService = new CategoryService(repositoryPort, eventPublisher);
    }

    @Test
    void shouldAllowCreatorSellerToDeleteOwnCategory() {
        UUID sellerAId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Category cat = new Category(categoryId, "Zapatos Artesanales", sellerAId);

        when(repositoryPort.findById(categoryId)).thenReturn(Optional.of(cat));

        boolean result = categoryService.delete(categoryId, sellerAId, "seller");

        assertTrue(result);
        verify(repositoryPort, times(1)).deleteById(categoryId);
    }

    @Test
    void shouldDenySellerBFromDeletingSellerACategory() {
        UUID sellerAId = UUID.randomUUID();
        UUID sellerBId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Category cat = new Category(categoryId, "Zapatos Artesanales", sellerAId);

        when(repositoryPort.findById(categoryId)).thenReturn(Optional.of(cat));

        SecurityException ex = assertThrows(SecurityException.class, () ->
                categoryService.delete(categoryId, sellerBId, "seller")
        );

        assertTrue(ex.getMessage().contains("creada por otro vendedor"));
        verify(repositoryPort, never()).deleteById(any());
    }

    @Test
    void shouldAllowAdminToDeleteAnyCategory() {
        UUID sellerAId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Category cat = new Category(categoryId, "Zapatos Artesanales", sellerAId);

        when(repositoryPort.findById(categoryId)).thenReturn(Optional.of(cat));

        boolean result = categoryService.delete(categoryId, adminId, "admin");

        assertTrue(result);
        verify(repositoryPort, times(1)).deleteById(categoryId);
    }
}
