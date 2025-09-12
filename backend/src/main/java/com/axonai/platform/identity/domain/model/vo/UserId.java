package com.axonai.platform.identity.domain.model.vo;

import java.util.UUID;

/**
 * Representa o identificador único de um usuário no sistema. Utiliza UUID para garantir unicidade
 * global.
 *
 * <p>Este record é imutável e encapsula a lógica de criação e validação do ID do usuário,
 * garantindo que apenas IDs válidos sejam instanciados.
 */
public record UserId(UUID value) {
    public UserId {
        if (value == null) {
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
            throw new IllegalArgumentException("Invalid UUID format for User ID.", e);
        }
    }

    @Override
    public String toString() {

        return value.toString();
    }
}
