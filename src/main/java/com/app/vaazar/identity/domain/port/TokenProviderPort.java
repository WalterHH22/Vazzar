package com.app.vaazar.identity.domain.port;

import com.app.vaazar.identity.domain.model.User;

import java.util.UUID;

public interface TokenProviderPort {
    String generateToken(User user);
    UUID extractUserId(String token);
    String extractEmail(String token);
    String extractRole(String token);
    boolean validateToken(String token);
}
