package com.app.vaazar.identity.application.usecase;

import com.app.vaazar.identity.application.dto.AuthResponse;
import com.app.vaazar.identity.application.dto.LoginCommand;
import com.app.vaazar.identity.domain.exception.InvalidCredentialsException;
import com.app.vaazar.identity.domain.exception.UserInactiveException;
import com.app.vaazar.identity.domain.model.User;
import com.app.vaazar.identity.domain.port.PasswordEncoderPort;
import com.app.vaazar.identity.domain.port.TokenProviderPort;
import com.app.vaazar.identity.domain.port.UserRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    public LoginUseCase(UserRepositoryPort userRepositoryPort,
                        PasswordEncoderPort passwordEncoderPort,
                        TokenProviderPort tokenProviderPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.tokenProviderPort = tokenProviderPort;
    }

    public AuthResponse execute(LoginCommand command) {
        if (command == null || command.getEmail() == null || command.getPassword() == null) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        User user = userRepositoryPort.findByEmail(command.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!user.isActive()) {
            throw new UserInactiveException("El usuario con email '" + user.getEmail() + "' se encuentra inactivo");
        }

        if (!passwordEncoderPort.matches(command.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = tokenProviderPort.generateToken(user);
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole().getValue());
    }
}
