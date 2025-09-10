package com.axonai.platform.domain.model.aggregate;

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

    // O construtor privado garante que a criação só é possível através de factory methods controlados.
    private UserAggregate(UserId userId, Email email, String hashedPassword, UserStatus status) {
        // Checagens básicas de nulidade podem ser consideradas uma invariante no momento da construção.
        this.userId = Objects.requireNonNull(userId, "User ID não pode ser nulo.");
        this.email = Objects.requireNonNull(email, "Email não pode ser nulo.");
        this.hashedPassword = Objects.requireNonNull(hashedPassword, "Senha com hash não pode ser nula.");
        this.status = Objects.requireNonNull(status, "Status do usuário não pode ser nulo.");
    }

    /**
     * Método de fábrica (static factory method) para criar um novo usuário.
     * Este método contém a lógica para a criação inicial do usuário,
     * como a definição de um status padrão.
     */
    public static UserAggregate register(Email email, String hashedPassword) {
        // Impõe o estado inicial de um usuário recém-registrado.
        return new UserAggregate(
                UserId.generate(), // Assumindo que UserId possui um método gerador estático.
                email,
                hashedPassword,
                UserStatus.PENDING_VERIFICATION
        );
    }

    // --- Métodos de Comportamento ---

    /**
     * Altera a senha do usuário, aplicando quaisquer regras de negócio relevantes.
     * @param newHashedPassword A nova senha, já com hash aplicado.
     */
    public void changePassword(String newHashedPassword) {
        // Invariante: Um usuário inativo não pode alterar sua senha.
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException("Não é possível alterar a senha de um usuário inativo.");
        }
        this.hashedPassword = Objects.requireNonNull(newHashedPassword, "A nova senha com hash não pode ser nula.");
    }

    /**
     * Ativa a conta de um usuário.
     */
    public void activate() {
        // Invariante: Apenas usuários com verificação pendente podem ser ativados.
        if (this.status != UserStatus.PENDING_VERIFICATION) {
            throw new IllegalStateException("O usuário não pode ser ativado a partir do status atual: " + this.status);
        }
        this.status = UserStatus.ACTIVE;
    }

    /**
     * Desativa a conta de um usuário.
     */
    public void deactivate() {
        // Invariante: Um usuário precisa estar ativo para ser desativado.
        if (this.status != UserStatus.ACTIVE) {
            throw new IllegalStateException("O usuário não pode ser desativado a partir do status atual: " + this.status);
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