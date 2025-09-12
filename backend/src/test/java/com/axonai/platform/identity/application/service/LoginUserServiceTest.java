package com.axonai.platform.identity.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.axonai.platform.identity.application.exception.AuthenticationFailureException;
import com.axonai.platform.identity.application.port.in.AuthenticationResult;
import com.axonai.platform.identity.application.port.in.LoginCommand;
import com.axonai.platform.identity.application.port.out.JwtProvider;
import com.axonai.platform.identity.application.port.out.JwtTokens;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;
import com.axonai.platform.identity.domain.model.vo.Email;
import com.axonai.platform.identity.domain.model.vo.HashedPassword;
import com.axonai.platform.identity.domain.service.PasswordPolicy;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginUserServiceTest {

    @Mock private UserRepositoryPort userRepositoryPort;

    @Mock private PasswordPolicy passwordPolicy;

    @Mock private JwtProvider jwtProvider;

    @InjectMocks private LoginUserService loginUserService;

    private LoginCommand command;

    private UserAggregate testUser;
    private Email testEmail;
    private HashedPassword testHashedPassword;
    private char[] password;

    @BeforeEach
    void setUp() {
        String emailStr = "test@example.com";
        password = "validPassword123".toCharArray();

        testEmail = new Email(emailStr);

        String validBcryptHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        testHashedPassword = new HashedPassword(validBcryptHash);

        testUser = UserAggregate.register(testEmail, testHashedPassword);
        testUser.activate();
    }

    @Test
    @DisplayName("Deve autenticar com sucesso um usuário ativo com credenciais válidas")
    void login_withValidCredentialsAndActiveUser_shouldReturnAuthenticationResult() {
        command = spy(new LoginCommand(testEmail.value(), password));

        JwtTokens tokens = new JwtTokens("access-token", "refresh-token", 3600L);

        when(userRepositoryPort.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordPolicy.verify(new String(command.password()), testHashedPassword))
                .thenReturn(true);
        when(jwtProvider.generateTokens(testUser)).thenReturn(tokens);

        AuthenticationResult result = loginUserService.login(command);

        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo("refresh-token");
        assertThat(result.expiresIn())
                .isEqualTo(3600L); // Assegura que o valor expiresIn também é verificado.
        assertThat(result.tokenType()).isEqualTo("Bearer");
        assertThat(result.user().id()).isEqualTo(testUser.getUserId().value().toString());
        assertThat(result.user().email()).isEqualTo(testEmail.value());

        verify(command).clearPassword();
    }

    @Test
    @DisplayName("Deve lançar AuthenticationFailureException para usuário inexistente")
    void login_withNonExistentUser_shouldThrowAuthenticationFailureException() {
        command = spy(new LoginCommand(testEmail.value(), password));
        when(userRepositoryPort.findByEmail(testEmail)).thenReturn(Optional.empty());
        when(passwordPolicy.verify(any(String.class), any(HashedPassword.class))).thenReturn(false);

        assertThatThrownBy(() -> loginUserService.login(command))
                .isInstanceOf(AuthenticationFailureException.class)
                .hasMessage("E-mail ou senha inválidos.");

        verify(jwtProvider, never()).generateTokens(any());
        verify(command).clearPassword();
    }

    @Test
    @DisplayName("Deve lançar AuthenticationFailureException para senha incorreta")
    void login_withExistingUserAndIncorrectPassword_shouldThrowAuthenticationFailureException() {
        command = spy(new LoginCommand(testEmail.value(), password));
        when(userRepositoryPort.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(passwordPolicy.verify(new String(command.password()), testHashedPassword))
                .thenReturn(false);

        assertThatThrownBy(() -> loginUserService.login(command))
                .isInstanceOf(AuthenticationFailureException.class)
                .hasMessage("E-mail ou senha inválidos.");

        verify(jwtProvider, never()).generateTokens(any());
        verify(command).clearPassword();
    }

    @Test
    @DisplayName("Deve lançar AuthenticationFailureException para usuário inativo")
    void login_withInactiveUser_shouldThrowAuthenticationFailureException() {
        command = spy(new LoginCommand(testEmail.value(), password));

        UserAggregate inactiveUser = UserAggregate.register(testEmail, testHashedPassword);
        inactiveUser.activate();
        inactiveUser.deactivate();

        when(userRepositoryPort.findByEmail(testEmail)).thenReturn(Optional.of(inactiveUser));
        when(passwordPolicy.verify(new String(command.password()), testHashedPassword))
                .thenReturn(true);

        assertThatThrownBy(() -> loginUserService.login(command))
                .isInstanceOf(AuthenticationFailureException.class)
                .hasMessage("E-mail ou senha inválidos.");

        verify(jwtProvider, never()).generateTokens(any());
        verify(command).clearPassword();
    }
}
