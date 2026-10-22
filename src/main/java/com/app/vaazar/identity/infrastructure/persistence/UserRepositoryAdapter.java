package com.app.vaazar.identity.infrastructure.persistence;

import com.app.vaazar.identity.domain.model.User;
import com.app.vaazar.identity.domain.model.UserRole;
import com.app.vaazar.identity.domain.model.UserStatus;
import com.app.vaazar.identity.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository jpaRepository;

    public UserRepositoryAdapter(SpringDataUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toEntity(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmailIgnoreCase(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmailIgnoreCase(email);
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                UserRole.fromString(entity.getRole()),
                UserStatus.fromString(entity.getStatus()),
                entity.getCreatedAt()
        );
    }

    private UserJpaEntity toEntity(User domain) {
        return new UserJpaEntity(
                domain.getId(),
                domain.getEmail(),
                domain.getPasswordHash(),
                domain.getRole().getValue(),
                domain.getStatus().getValue(),
                domain.getCreatedAt()
        );
    }
}
