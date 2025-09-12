package com.axonai.platform.identity.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.axonai.platform.identity.application.exception.ConflictException;
import com.axonai.platform.identity.application.port.in.RegisterUserCommand;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;
import com.axonai.platform.identity.domain.model.enums.UserStatus;
import com.axonai.platform.identity.domain.model.vo.Email;
import com.axonai.platform.identity.domain.model.vo.HashedPassword;
import com.axonai.platform.identity.domain.service.PasswordPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock private UserRepositoryPort userRepositoryPort;

    @Mock private PasswordPolicy passwordPolicy;

    @InjectMocks private RegisterUserService registerUserService;

    @Captor private ArgumentCaptor<UserAggregate> userAggregateCaptor;

    private RegisterUserCommand command;
    private Email testEmail;
    private HashedPassword testHashedPassword;
    private String emailStr;
    private String passwordStr;

    @BeforeEach
    void setUp() {
        emailStr = "newuser@example.com";
        passwordStr = "strongPassword!123";
        char[] password = passwordStr.toCharArray(); // O comando ainda precisa do char[]

        command = spy(new RegisterUserCommand(emailStr, password));
        testEmail = new Email(emailStr);
        String validBcryptHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        testHashedPassword = new HashedPassword(validBcryptHash);
    }

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso quando o e-mail for único")
    void registerUser_withUniqueEmail_shouldHashPasswordAndSaveNewUser() {
        when(userRepositoryPort.existsByEmail(testEmail)).thenReturn(false);
        when(passwordPolicy.hash(passwordStr)).thenReturn(testHashedPassword);

        registerUserService.registerUser(command);

        verify(userRepositoryPort).existsByEmail(testEmail);

        verify(passwordPolicy).hash(passwordStr);

        verify(command).clearPassword();
        verify(userRepositoryPort).save(userAggregateCaptor.capture());

        UserAggregate savedUser = userAggregateCaptor.getValue();
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(testEmail);
        assertThat(savedUser.getHashedPassword()).isEqualTo(testHashedPassword);
        assertThat(savedUser.getStatus()).isEqualTo(UserStatus.PENDING_VERIFICATION);
    }

    @Test
    @DisplayName("Deve lançar ConflictException quando o e-mail já estiver em uso")
    void registerUser_withExistingEmail_shouldThrowConflictException() {
        when(userRepositoryPort.existsByEmail(testEmail)).thenReturn(true);

        assertThatThrownBy(() -> registerUserService.registerUser(command))
                .isInstanceOf(ConflictException.class)
                .hasMessage("O e-mail '%s' já está em uso.".formatted(emailStr));

        verify(passwordPolicy, never()).hash(any(String.class));
        verify(userRepositoryPort, never()).save(any(UserAggregate.class));
        verify(command, never()).clearPassword();
    }
}
