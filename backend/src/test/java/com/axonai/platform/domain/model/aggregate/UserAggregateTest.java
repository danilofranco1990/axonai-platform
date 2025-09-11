package com.axonai.platform.domain.model.aggregate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.axonai.platform.domain.exception.InvalidUserStatusTransitionException;
import com.axonai.platform.domain.exception.UserInactiveException;
import com.axonai.platform.domain.exception.UserNotVerifiedException;
import com.axonai.platform.domain.model.enums.UserStatus;
import com.axonai.platform.domain.model.vo.Email;
import com.axonai.platform.domain.model.vo.HashedPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserAggregateTest {

    // Usar constantes para dados de teste melhora a legibilidade e a manutenção.
    // Estes são hashes Bcrypt estruturalmente válidos.
    private static final HashedPassword VALID_HASH_1 =
            new HashedPassword("$2a$10$h.dl5J86rGH7I8Glpde9v.wS5KGYs.I2tcm2J1vS9y5.f6vdeIeS2");
    private static final HashedPassword VALID_HASH_2 =
            new HashedPassword("$2a$10$3g5v1hG.4Phc5JAbM0x5J.E9w8v8t7b6u5a4f3g2h1j0k9l8m7n6o");

    private Email testEmail;

    @BeforeEach
    void setUp() {
        testEmail = new Email("test@example.com");
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com status PENDING_VERIFICATION")
    void shouldRegisterNewUserWithPendingStatus() {
        // When
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(testEmail);
        assertThat(user.getHashedPassword()).isEqualTo(VALID_HASH_1);
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING_VERIFICATION);
    }

    @Test
    @DisplayName("Deve ativar um usuário com status PENDING_VERIFICATION")
    void shouldActivateUserWhenStatusIsPending() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);

        // When
        user.activate();

        // Then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar ativar um usuário que já está ACTIVE")
    void shouldThrowExceptionWhenActivatingAnActiveUser() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);
        user.activate(); // Status is now ACTIVE

        // When / Then
        assertThatThrownBy(user::activate)
                .isInstanceOf(InvalidUserStatusTransitionException.class)
                .hasMessageContaining(
                        "O usuário não pode ser ativado a partir do status atual: ACTIVE");
    }

    @Test
    @DisplayName("Deve desativar um usuário com status ACTIVE")
    void shouldDeactivateUserWhenStatusIsActive() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);
        user.activate(); // Status is now ACTIVE

        // When
        user.deactivate();

        // Then
        assertThat(user.getStatus()).isEqualTo(UserStatus.INACTIVE);
    }



    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar um usuário que não está ACTIVE")
    void shouldThrowExceptionWhenDeactivatingANonActiveUser() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1); // Status is PENDING

        // When / Then
        assertThatThrownBy(user::deactivate)
                .isInstanceOf(InvalidUserStatusTransitionException.class)
                .hasMessageContaining(
                        "O usuário não pode ser desativado a partir do status atual: PENDING_VERIFICATION");
    }

    @Test
    @DisplayName("Deve permitir a troca de senha para um usuário ACTIVE")
    void shouldAllowPasswordChangeForActiveUser() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);
        user.activate();

        // When
        user.changePassword(VALID_HASH_2);

        // Then
        assertThat(user.getHashedPassword()).isEqualTo(VALID_HASH_2);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar trocar a senha de um usuário INACTIVE")
    void shouldThrowExceptionWhenChangingPasswordForInactiveUser() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);
        user.activate();
        user.deactivate(); // Status is now INACTIVE

        // When / Then
        assertThatThrownBy(() -> user.changePassword(VALID_HASH_2))
                .isInstanceOf(UserInactiveException.class)
                .hasMessage("Não é possível alterar a senha de um usuário inativo.");
    }

    @Test
    @DisplayName("Deve passar na verificação de estado quando o usuário está ACTIVE")
    void ensureIsActive_shouldPass_whenUserIsActive() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);
        user.activate(); // Status é ACTIVE

        // When / Then
        assertDoesNotThrow(user::ensureIsActive);
    }

    @Test
    @DisplayName("Deve lançar UserInactiveException quando o usuário está INACTIVE")
    void ensureIsActive_shouldThrowUserInactiveException_whenUserIsInactive() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);
        user.activate();
        user.deactivate(); // Status é INACTIVE

        // When / Then
        assertThatThrownBy(user::ensureIsActive)
                .isInstanceOf(UserInactiveException.class)
                .hasMessage("A operação não pode ser executada pois o usuário está inativo.");
    }

    @Test
    @DisplayName("Deve lançar UserNotVerifiedException quando o usuário está PENDING_VERIFICATION")
    void ensureIsActive_shouldThrowUserNotVerifiedException_whenUserIsPending() {
        // Given
        UserAggregate user = UserAggregate.register(testEmail, VALID_HASH_1);

        // When / Then
        assertThatThrownBy(user::ensureIsActive)
                .isInstanceOf(UserNotVerifiedException.class)
                .hasMessage(
                        "A operação não pode ser executada pois o usuário ainda não verificou a sua conta.");
    }
}