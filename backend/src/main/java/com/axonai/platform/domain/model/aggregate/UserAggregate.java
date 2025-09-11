package com.axonai.platform.domain.model.aggregate;

import com.axonai.platform.domain.exception.InvalidUserStatusTransitionException;
import com.axonai.platform.domain.exception.UserInactiveException;
import com.axonai.platform.domain.exception.UserNotVerifiedException;
import com.axonai.platform.domain.model.enums.UserStatus;
import com.axonai.platform.domain.model.vo.Email;
import com.axonai.platform.domain.model.vo.HashedPassword;
import com.axonai.platform.domain.model.vo.UserId;
import java.time.Instant;
import java.util.Objects;

/**
 * Representa a raiz de agregado (aggregate root) User. A responsabilidade do agregado é impor
 * invariantes (regras de negócio) para todas as mudanças de estado. O estado é manipulado
 * exclusivamente através de métodos ricos em comportamento, não por setters públicos.
 */
public class UserAggregate {

    private final UserId userId;
    private final Email email;
    private HashedPassword hashedPassword;
    private UserStatus status;
    private final Instant createdAt;
    private Instant updatedAt;
    private Long version;

    private UserAggregate(
            UserId userId, Email email, HashedPassword hashedPassword, UserStatus status) {
        this.userId = Objects.requireNonNull(userId, "User ID não pode ser nulo.");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo.");
        this.hashedPassword =
                Objects.requireNonNull(hashedPassword, "Senha com hash não pode ser nula.");
        this.status = Objects.requireNonNull(status, "Status do usuário não pode ser nulo.");
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public static UserAggregate register(Email email, HashedPassword hashedPassword) {
        return new UserAggregate(
                UserId.generate(), email, hashedPassword, UserStatus.PENDING_VERIFICATION);
    }

    public void changePassword(HashedPassword newHashedPassword) {
        if (this.status == UserStatus.INACTIVE) {
            throw new UserInactiveException(
                    "Não é possível alterar a senha de um usuário inativo.");
        }
        this.hashedPassword =
                Objects.requireNonNull(
                        newHashedPassword, "A nova senha com hash não pode ser nula.");
        this.touch();
    }

    public void activate() {
        if (this.status != UserStatus.PENDING_VERIFICATION) {
            throw new InvalidUserStatusTransitionException(
                    "O usuário não pode ser ativado a partir do status atual: " + this.status);
        }
        this.status = UserStatus.ACTIVE;
        this.touch();
    }

    public void deactivate() {
        if (this.status != UserStatus.ACTIVE) {
            throw new InvalidUserStatusTransitionException(
                    "O usuário não pode ser desativado a partir do status atual: " + this.status);
        }
        this.status = UserStatus.INACTIVE;
        this.touch();
    }

    /**
     * Garante que o usuário está no estado ACTIVE. Lança uma exceção de domínio específica caso
     * contrário. Este é um "guard method" para ser usado por serviços de aplicação.
     */
    public void ensureIsActive() {
        if (this.status == UserStatus.INACTIVE) {
            throw new UserInactiveException(
                    "A operação não pode ser executada pois o usuário está inativo.");
        }
        if (this.status == UserStatus.PENDING_VERIFICATION) {
            throw new UserNotVerifiedException(
                    "A operação não pode ser executada pois o usuário ainda não verificou a sua conta.");
        }
    }

    // Método privado para centralizar a atualização do timestamp
    private void touch() {
        this.updatedAt = Instant.now();
    }

    // --- Getters ---

    public UserId getUserId() {
        return userId;
    }

    public Email getEmail() {
        return email;
    }

    public HashedPassword getHashedPassword() {
        return hashedPassword;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    // --- Sobrescritas para comparação de identidade ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAggregate that = (UserAggregate) o;
        return userId.equals(that.userId); // Agregados são comparados por seu ID.
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
