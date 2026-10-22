package com.app.vaazar.identity.application.usecase;

import com.app.vaazar.identity.application.dto.AuthResponse;
import com.app.vaazar.identity.application.dto.RegisterCommand;
import com.app.vaazar.identity.domain.exception.UserAlreadyExistsException;
import com.app.vaazar.identity.domain.model.User;
import com.app.vaazar.identity.domain.model.UserRole;
import com.app.vaazar.identity.domain.model.UserStatus;
import com.app.vaazar.identity.domain.port.PasswordEncoderPort;
import com.app.vaazar.identity.domain.port.TokenProviderPort;
import com.app.vaazar.identity.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public RegisterUserUseCase(UserRepositoryPort userRepositoryPort,
                               PasswordEncoderPort passwordEncoderPort,
                               TokenProviderPort tokenProviderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    public AuthResponse execute(RegisterCommand command) {
        String normalizedEmail = command.getEmail().trim().toLowerCase();

        if (userRepositoryPort.existsByEmail(normalizedEmail)) {
            throw new UserAlreadyExistsException("El email '" + normalizedEmail + "' ya se encuentra registrado");
        }

        String passwordHash = passwordEncoderPort.encode(command.getPassword());
        UserRole role = command.getRole() != null ? UserRole.fromString(command.getRole()) : UserRole.BUYER;

        User user = new User(
                UUID.randomUUID(),
                normalizedEmail,
                passwordHash,
                role,
                UserStatus.ACTIVE,
                Instant.now()
        );

        User savedUser = userRepositoryPort.save(user);
        String token = tokenProviderPort.generateToken(savedUser);

        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getRole().getValue());
    }
}
