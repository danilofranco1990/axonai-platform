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

@ExtendWith(MockitoExtension.class)
class BcryptPasswordPolicyTest {

    @Mock private PasswordEncoder mockPasswordEncoder;

    private BcryptPasswordPolicy passwordPolicy;

    @BeforeEach
    void setUp() {
        passwordPolicy = new BcryptPasswordPolicy(mockPasswordEncoder);
    }

    @Test
    @DisplayName("Deve chamar o método encode e retornar um HashedPassword válido")
    void hash_shouldDelegateToEncoderAndWrapResult() {
        String rawPassword = "plainTextPassword123";
        String expectedHash = "$2a$10$h.dl5J86rGH7I8Glpde9v.wS5KGYs.I2tcm2J1vS9y5.f6vdeIeS2";

        when(mockPasswordEncoder.encode(rawPassword)).thenReturn(expectedHash);

        HashedPassword result = passwordPolicy.hash(rawPassword);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(expectedHash);
        verify(mockPasswordEncoder).encode(rawPassword);
    }

    @Test
    @DisplayName("Deve chamar o método matches e retornar true para senhas correspondentes")
    void verify_shouldDelegateToEncoderAndReturnTrueForMatchingPasswords() {
        String rawPassword = "plainTextPassword123";
        HashedPassword hashedPassword =
                new HashedPassword("$2a$10$h.dl5J86rGH7I8Glpde9v.wS5KGYs.I2tcm2J1vS9y5.f6vdeIeS2");

        when(mockPasswordEncoder.matches(rawPassword, hashedPassword.value())).thenReturn(true);

        boolean result = passwordPolicy.verify(rawPassword, hashedPassword);

        assertThat(result).isTrue();
        verify(mockPasswordEncoder).matches(rawPassword, hashedPassword.value());
    }

    @Test
    @DisplayName("Deve chamar o método matches e retornar false para senhas não correspondentes")
    void verify_shouldDelegateToEncoderAndReturnFalseForNonMatchingPasswords() {
        String rawPassword = "wrongPassword";
        HashedPassword hashedPassword =
                new HashedPassword("$2a$10$h.dl5J86rGH7I8Glpde9v.wS5KGYs.I2tcm2J1vS9y5.f6vdeIeS2");

        when(mockPasswordEncoder.matches(rawPassword, hashedPassword.value())).thenReturn(false);

        boolean result = passwordPolicy.verify(rawPassword, hashedPassword);

        assertThat(result).isFalse();
        verify(mockPasswordEncoder).matches(rawPassword, hashedPassword.value());
    }
}
