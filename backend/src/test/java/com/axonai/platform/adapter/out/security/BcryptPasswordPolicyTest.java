package com.axonai.platform.adapter.out.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.axonai.platform.domain.model.vo.HashedPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class) // Habilita o uso de anotações do Mockito
class BcryptPasswordPolicyTest {

    @Mock private PasswordEncoder mockPasswordEncoder; // Cria um mock da dependência

    private BcryptPasswordPolicy passwordPolicy; // Nossa classe sob teste (SUT)

    @BeforeEach
    void setUp() {
        // Injeta o mock na nossa classe antes de cada teste
        passwordPolicy = new BcryptPasswordPolicy(mockPasswordEncoder);
    }

    @Test
    @DisplayName("Deve chamar o método encode e retornar um HashedPassword válido")
    void hash_shouldDelegateToEncoderAndWrapResult() {
        // Arrange (Preparação)
        String rawPassword = "plainTextPassword123";
        String expectedHash = "$2a$10$h.dl5J86rGH7I8Glpde9v.wS5KGYs.I2tcm2J1vS9y5.f6vdeIeS2";

        // "Quando o método encode do mock for chamado com a senha, retorne o hash esperado"
        when(mockPasswordEncoder.encode(rawPassword)).thenReturn(expectedHash);

        // Act (Ação)
        HashedPassword result = passwordPolicy.hash(rawPassword);

        // Assert (Verificação)
        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(expectedHash);
        // "Verifique se o método encode do mock foi chamado exatamente uma vez com o argumento
        // correto"
        verify(mockPasswordEncoder).encode(rawPassword);
    }

    @Test
    @DisplayName("Deve chamar o método matches e retornar true para senhas correspondentes")
    void verify_shouldDelegateToEncoderAndReturnTrueForMatchingPasswords() {
        // Arrange
        String rawPassword = "plainTextPassword123";
        HashedPassword hashedPassword =
                new HashedPassword("$2a$10$h.dl5J86rGH7I8Glpde9v.wS5KGYs.I2tcm2J1vS9y5.f6vdeIeS2");

        // "Quando o método matches do mock for chamado, retorne true"
        when(mockPasswordEncoder.matches(rawPassword, hashedPassword.value())).thenReturn(true);

        // Act
        boolean result = passwordPolicy.verify(rawPassword, hashedPassword);

        // Assert
        assertThat(result).isTrue();
        // "Verifique se o método matches foi chamado exatamente uma vez com os argumentos corretos"
        verify(mockPasswordEncoder).matches(rawPassword, hashedPassword.value());
    }

    @Test
    @DisplayName("Deve chamar o método matches e retornar false para senhas não correspondentes")
    void verify_shouldDelegateToEncoderAndReturnFalseForNonMatchingPasswords() {
        // Arrange
        String rawPassword = "wrongPassword";
        HashedPassword hashedPassword =
                new HashedPassword("$2a$10$h.dl5J86rGH7I8Glpde9v.wS5KGYs.I2tcm2J1vS9y5.f6vdeIeS2");

        // "Quando o método matches do mock for chamado, retorne false"
        when(mockPasswordEncoder.matches(rawPassword, hashedPassword.value())).thenReturn(false);

        // Act
        boolean result = passwordPolicy.verify(rawPassword, hashedPassword);

        // Assert
        assertThat(result).isFalse();
        verify(mockPasswordEncoder).matches(rawPassword, hashedPassword.value());
    }
}
