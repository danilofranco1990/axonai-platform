package com.axonai.platform.domain.model.aggregate;

import com.axonai.platform.domain.exception.InvalidUserStatusTransitionException;
import com.axonai.platform.domain.exception.UserInactiveException;
import com.axonai.platform.domain.model.enums.UserStatus;
import com.axonai.platform.domain.model.vo.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserAggregateTest {

    @Test
    @DisplayName("Deve registrar um novo usuário com status PENDING_VERIFICATION")
    void shouldRegisterNewUserWithPendingStatus() {
        // Given
        Email email = new Email("test@example.com");
        String hashedPassword = "hashedPassword123";

        // When
        UserAggregate user = UserAggregate.register(email, hashedPassword);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getHashedPassword()).isEqualTo(hashedPassword);
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING_VERIFICATION);
    }

    @Test
    @DisplayName("Deve ativar um usuário com status PENDING_VERIFICATION")
    void shouldActivateUserWhenStatusIsPending() {
        // Given
        UserAggregate user = UserAggregate.register(new Email("test@example.com"), "hashed");

        // When
        user.activate();

        // Then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar ativar um usuário que já está ACTIVE")
    void shouldThrowExceptionWhenActivatingAnActiveUser() {
        // Given
        UserAggregate user = UserAggregate.register(new Email("test@example.com"), "hashed");
        user.activate(); // Status is now ACTIVE

        // When / Then
        assertThatThrownBy(user::activate)
                .isInstanceOf(InvalidUserStatusTransitionException.class)
                .hasMessageContaining("O usuário não pode ser ativado a partir do status atual: ACTIVE");
    }

    @Test
    @DisplayName("Deve desativar um usuário com status ACTIVE")
    void shouldDeactivateUserWhenStatusIsActive() {
        // Given
        UserAggregate user = UserAggregate.register(new Email("test@example.com"), "hashed");
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
        UserAggregate user = UserAggregate.register(new Email("test@example.com"), "hashed"); // Status is PENDING

        // When / Then
        assertThatThrownBy(user::deactivate)
                .isInstanceOf(InvalidUserStatusTransitionException.class)
                .hasMessageContaining("O usuário não pode ser desativado a partir do status atual: PENDING_VERIFICATION");
    }

    @Test
    @DisplayName("Deve permitir a troca de senha para um usuário ACTIVE")
    void shouldAllowPasswordChangeForActiveUser() {
        // Given
        UserAggregate user = UserAggregate.register(new Email("test@example.com"), "hashed");
        user.activate();
        String newHashedPassword = "newHashedPassword456";

        // When
        user.changePassword(newHashedPassword);

        // Then
        assertThat(user.getHashedPassword()).isEqualTo(newHashedPassword);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar trocar a senha de um usuário INACTIVE")
    void shouldThrowExceptionWhenChangingPasswordForInactiveUser() {
        // Given
        UserAggregate user = UserAggregate.register(new Email("test@example.com"), "hashed");
        user.activate();
        user.deactivate(); // Status is now INACTIVE

        // When / Then
        assertThatThrownBy(() -> user.changePassword("newHashedPassword"))
                .isInstanceOf(UserInactiveException.class)
                .hasMessage("Não é possível alterar a senha de um usuário inativo.");
    }
}