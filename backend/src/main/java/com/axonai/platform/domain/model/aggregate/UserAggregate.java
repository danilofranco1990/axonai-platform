package com.axonai.platform.domain.model.aggregate;

import com.axonai.platform.domain.exception.UserDomainException;
import com.axonai.platform.domain.model.enums.UserStatus;
import com.axonai.platform.domain.model.vo.Email;
import com.axonai.platform.domain.model.vo.UserId;

import java.util.Objects;

/**
 * Representa a raiz de agregado (aggregate root) User.
 * A responsabilidade do agregado é impor invariantes (regras de negócio)
 * para todas as mudanças de estado. O estado é manipulado exclusivamente através
 * de métodos ricos em comportamento, não por setters públicos.
 */
public class UserAggregate {

    private final UserId userId;
    private final Email email;
    private String hashedPassword;
    private UserStatus status;

    private UserAggregate(UserId userId, Email email, String hashedPassword, UserStatus status) {
        this.userId = Objects.requireNonNull(userId, "User ID não pode ser nulo.");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo.");
        this.hashedPassword = Objects.requireNonNull(hashedPassword, "Senha com hash não pode ser nula.");
        this.status = Objects.requireNonNull(status, "Status do usuário não pode ser nulo.");
    }

    public static UserAggregate register(Email email, String hashedPassword) {
        return new UserAggregate(
                UserId.generate(),
                email,
                hashedPassword,
                UserStatus.PENDING_VERIFICATION
        );
    }

    public void changePassword(String newHashedPassword) {
        if (this.status == UserStatus.INACTIVE) {
            // Refatorado para usar a exceção de domínio
            throw new UserDomainException("Não é possível alterar a senha de um usuário inativo.");
        }
        this.hashedPassword = Objects.requireNonNull(newHashedPassword, "A nova senha com hash não pode ser nula.");
    }

    public void activate() {
        if (this.status != UserStatus.PENDING_VERIFICATION) {
            // Refatorado para usar a exceção de domínio
            throw new UserDomainException("O usuário não pode ser ativado a partir do status atual: " + this.status);
        }
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status != UserStatus.ACTIVE) {
            // Refatorado para usar a exceção de domínio
            throw new UserDomainException("O usuário não pode ser desativado a partir do status atual: " + this.status);
        }
        this.status = UserStatus.INACTIVE;
    }


    // --- Getters ---

    public UserId getUserId() {
        return userId;
    }

    public Email getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public UserStatus getStatus() {
        return status;
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