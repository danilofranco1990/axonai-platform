
package com.axonai.platform.application.port.in;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginCommandTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar LoginCommand com valores válidos")
    void deveCriarLoginCommandValido() {
        char[] senha = "password123".toCharArray();
        LoginCommand cmd = new LoginCommand("user@email.com", senha);

        assertEquals("user@email.com", cmd.email());
        assertArrayEquals(senha, cmd.password());
    }

    @Test
    @DisplayName("Deve validar e-mail vazio")
    void deveValidarEmailVazio() {
        LoginCommand cmd = new LoginCommand("", "password123".toCharArray());
        Set<ConstraintViolation<LoginCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Deve validar e-mail inválido")
    void deveValidarEmailInvalido() {
        LoginCommand cmd = new LoginCommand("email-invalido", "password123".toCharArray());
        Set<ConstraintViolation<LoginCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Deve validar senha nula")
    void deveValidarSenhaNula() {
        LoginCommand cmd = new LoginCommand("user@email.com", null);
        Set<ConstraintViolation<LoginCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Deve validar senha com menos de 8 caracteres")
    void deveValidarSenhaCurta() {
        LoginCommand cmd = new LoginCommand("user@email.com", "short".toCharArray());
        Set<ConstraintViolation<LoginCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Deve limpar a senha com clearPassword")
    void deveLimparSenhaComClearPassword() {
        char[] senha = "password123".toCharArray();
        LoginCommand cmd = new LoginCommand("user@email.com", senha);

        cmd.clearPassword();

        for (char c : cmd.password()) {
            assertEquals('\0', c);
        }
    }
}