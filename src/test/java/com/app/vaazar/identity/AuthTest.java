package com.app.vaazar.identity;

import com.app.vaazar.identity.application.dto.AuthResponse;
import com.app.vaazar.identity.application.dto.LoginCommand;
import com.app.vaazar.identity.application.dto.RegisterCommand;
import com.app.vaazar.identity.application.usecase.LoginUseCase;
import com.app.vaazar.identity.application.usecase.RegisterUserUseCase;
import com.app.vaazar.identity.domain.exception.InvalidCredentialsException;
import com.app.vaazar.identity.domain.exception.UserAlreadyExistsException;
import com.app.vaazar.identity.domain.model.User;
import com.app.vaazar.identity.domain.port.PasswordEncoderPort;
import com.app.vaazar.identity.domain.port.TokenProviderPort;
import com.app.vaazar.identity.domain.port.UserRepositoryPort;
import com.app.vaazar.identity.infrastructure.security.BCryptPasswordEncoderAdapter;
import com.app.vaazar.identity.infrastructure.security.JwtTokenProviderAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthTest {

    private UserRepositoryPort userRepository;
    private PasswordEncoderPort passwordEncoder;
    private TokenProviderPort tokenProvider;
    private RegisterUserUseCase registerUserUseCase;
    private LoginUseCase loginUseCase;

    // Repositorio en memoria para pruebas unitarias rápidas
    private static class InMemoryUserRepository implements UserRepositoryPort {
        private final Map<UUID, User> usersById = new HashMap<>();
        private final Map<String, User> usersByEmail = new HashMap<>();

        @Override
        public User save(User user) {
            usersById.put(user.getId(), user);
            usersByEmail.put(user.getEmail().toLowerCase(), user);
            return user;
        }

        @Override
        public Optional<User> findById(UUID id) {
            return Optional.ofNullable(usersById.get(id));
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.ofNullable(usersByEmail.get(email.toLowerCase()));
        }

        @Override
        public boolean existsByEmail(String email) {
            return usersByEmail.containsKey(email.toLowerCase());
        }
    }

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        passwordEncoder = new BCryptPasswordEncoderAdapter();
        tokenProvider = new JwtTokenProviderAdapter(
                "vazzarSecretKeyForJwtAuthenticationMustBeAtLeast256BitsLongForHmacSha256Security2026",
                3600000L
        );
        registerUserUseCase = new RegisterUserUseCase(userRepository, passwordEncoder, tokenProvider);
        loginUseCase = new LoginUseCase(userRepository, passwordEncoder, tokenProvider);
    }

    @Test
    void shouldRegisterUserAndReturnValidJwt() {
        RegisterCommand command = new RegisterCommand("seller@empresa.com", "password123", "seller");
        AuthResponse response = registerUserUseCase.execute(command);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getUserId());
        assertEquals("seller@empresa.com", response.getEmail());
        assertEquals("seller", response.getRole());

        // Verificar token JWT
        assertTrue(tokenProvider.validateToken(response.getToken()));
        assertEquals(response.getUserId(), tokenProvider.extractUserId(response.getToken()));
        assertEquals("seller", tokenProvider.extractRole(response.getToken()));
    }

    @Test
    void shouldNotAllowDuplicateEmailRegistration() {
        RegisterCommand command = new RegisterCommand("seller@empresa.com", "password123", "seller");
        registerUserUseCase.execute(command);

        // Intentar registrar el mismo email (incluso con mayúsculas)
        RegisterCommand duplicate = new RegisterCommand("SELLER@empresa.com", "otraClave456", "buyer");
        assertThrows(UserAlreadyExistsException.class, () -> registerUserUseCase.execute(duplicate));
    }

    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        // Primero registramos al usuario
        registerUserUseCase.execute(new RegisterCommand("buyer@mail.com", "secreta123", "buyer"));

        // Luego ejecutamos login
        LoginCommand loginCommand = new LoginCommand("buyer@mail.com", "secreta123");
        AuthResponse loginResponse = loginUseCase.execute(loginCommand);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        assertEquals("buyer@mail.com", loginResponse.getEmail());
        assertEquals("buyer", loginResponse.getRole());
        assertTrue(tokenProvider.validateToken(loginResponse.getToken()));
    }

    @Test
    void shouldRejectLoginWithWrongPassword() {
        registerUserUseCase.execute(new RegisterCommand("user@mail.com", "claveCorrecta", "buyer"));

        LoginCommand wrongPass = new LoginCommand("user@mail.com", "claveIncorrecta");
        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.execute(wrongPass));
    }

    @Test
    void shouldRejectLoginWithNonExistentEmail() {
        LoginCommand nonExistent = new LoginCommand("noexiste@mail.com", "cualquiera");
        assertThrows(InvalidCredentialsException.class, () -> loginUseCase.execute(nonExistent));
    }
}
