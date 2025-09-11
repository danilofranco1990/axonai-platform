package com.axonai.platform.domain.model.vo;

import java.util.UUID;

// A interface Serializable foi removida por ser uma dependência técnica que raramente é a melhor
// escolha.
public record UserId(UUID value) {
    public UserId {
        if (value == null) {
            // A mensagem é mais precisa ao nomear o parâmetro específico.
            throw new IllegalArgumentException("UUID value for UserId cannot be null.");
        }
    }

    /** Gera um novo UserId único. */
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    /**
     * Cria um UserId a partir de uma representação de String. Essencial para reconstruir o objeto a
     * partir de bancos de dados ou DTOs (Data Transfer Objects).
     */
    public static UserId fromString(String uuid) {
        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("User ID string cannot be null or blank.");
        }
        try {
            return new UserId(UUID.fromString(uuid));
        } catch (IllegalArgumentException e) {
            // Encapsula a exceção original para fornecer contexto completo de depuração.
            throw new IllegalArgumentException("Invalid UUID format for User ID.", e);
        }
    }

    @Override
    public String toString() {
        // A implementação padrão do record é "UserId[value=...]",
        // que é útil para depuração, mas retornar a string pura do UUID
        // é frequentemente mais prático para logs e serialização.
        return value.toString();
    }
}
