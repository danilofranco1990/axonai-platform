package com.axonai.platform.domain.model.vo;

import static org.junit.jupiter.api.Assertions.*;

import com.axonai.platform.domain.exception.UserDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testes unitários para o Value Object HashedPassword. Garante que o contrato de validação do
 * objeto é rigorosamente seguido.
 */
@DisplayName("Value Object - HashedPassword")
class HashedPasswordTest {

    // Exemplo de um hash Bcrypt válido para ser usado nos testes.
    private final String VALID_BCRYPT_HASH =
            "$2a$12$R9h/cIPz0e.uRE2rf46gkuEt2i7./kYen172f2Icl3s1i8qtL5i3e";

    @Test
    @DisplayName("Deve criar instância com sucesso para um hash Bcrypt válido")
    void shouldCreateInstanceSuccessfullyForValidBcryptHash() {
        // Ação e Verificação
        // O construtor não deve lançar exceção para um valor válido.
        HashedPassword hashedPassword =
                assertDoesNotThrow(() -> new HashedPassword(VALID_BCRYPT_HASH));

        // Verifica se o valor foi armazenado corretamente.
        assertNotNull(hashedPassword);
        assertEquals(VALID_BCRYPT_HASH, hashedPassword.value());
    }

    @Test
    @DisplayName("Deve lançar NullPointerException quando o valor do hash for nulo")
    void shouldThrowNullPointerExceptionWhenHashValueIsNull() {
        // Ação e Verificação
        // A validação `Objects.requireNonNull` deve lançar NullPointerException.
        var exception = assertThrows(NullPointerException.class, () -> new HashedPassword(null));

        assertEquals("O valor da senha com hash não pode ser nulo.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(
            strings = {
                "", // String vazia
                "   ", // String em branco
                "senha_sem_hash", // String aleatória
                "123456789012345678901234567890", // String com formato incorreto
                "$2a$12$invalidlength", // Comprimento incorreto
                "$3a$12$R9h/cIPz0e.uRE2rf46gkuEt2i7./kYen172f2Icl3s1i8qtL5i3e", // Prefixo inválido
                " $2a$12$R9h/cIPz0e.uRE2rf46gkuEt2i7./kYen172f2Icl3s1i8qtL5i3e" // Com espaço no
                // início
            })
    @DisplayName("Deve lançar UserDomainException para formatos de hash inválidos")
    void shouldThrowUserDomainExceptionForInvalidHashFormats(String invalidHash) {
        // Ação e Verificação
        // A validação com a expressão regular deve falhar.
        var exception =
                assertThrows(UserDomainException.class, () -> new HashedPassword(invalidHash));

        assertEquals("Formato de hash de senha inválido.", exception.getMessage());
    }

    @Test
    @DisplayName("toString() deve retornar um valor redigido para segurança")
    void toStringShouldReturnRedactedValueForSecurity() {
        // Preparação
        HashedPassword hashedPassword = new HashedPassword(VALID_BCRYPT_HASH);

        // Ação
        String toStringOutput = hashedPassword.toString();

        // Verificação
        assertEquals("HashedPassword[value=REDACTED]", toStringOutput);
        assertFalse(
                toStringOutput.contains(VALID_BCRYPT_HASH),
                "O método toString não deve vazar o valor do hash.");
    }
}
