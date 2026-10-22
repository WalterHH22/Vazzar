package com.app.vaazar.identity.infrastructure.persistence;

import com.app.vaazar.identity.domain.model.User;
import com.app.vaazar.identity.domain.model.UserRole;
import com.app.vaazar.identity.domain.model.UserStatus;
import com.app.vaazar.identity.domain.port.PasswordEncoderPort;
import com.app.vaazar.identity.domain.port.UserRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class IdentityDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(IdentityDataInitializer.class);

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public IdentityDataInitializer(UserRepositoryPort userRepositoryPort, PasswordEncoderPort passwordEncoderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public void run(String... args) {
        seedUserIfNotExists("admin@vazzar.com", "admin123", UserRole.ADMIN);
        seedUserIfNotExists("seller@vazzar.com", "seller123", UserRole.SELLER);
        seedUserIfNotExists("buyer@vazzar.com", "buyer123", UserRole.BUYER);

        // Comerciantes y Vendedores de la Feria Virtual
        seedUserIfNotExists("harold_orellana@siman.com", "admin123", UserRole.SELLER);
        seedUserIfNotExists("calzado@vazzar.com", "seller123", UserRole.SELLER);
        seedUserIfNotExists("cafegourmet@vazzar.com", "seller123", UserRole.SELLER);
        seedUserIfNotExists("moda@vazzar.com", "seller123", UserRole.SELLER);
    }

    private void seedUserIfNotExists(String email, String rawPassword, UserRole role) {
        if (!userRepositoryPort.existsByEmail(email)) {
            User user = new User(
                    UUID.randomUUID(),
                    email,
                    passwordEncoderPort.encode(rawPassword),
                    role,
                    UserStatus.ACTIVE,
                    Instant.now()
            );
            userRepositoryPort.save(user);
            log.info("Usuario inicial creado en PostgreSQL -> email: {}, rol: {}", email, role.getValue());
        }
    }
}
