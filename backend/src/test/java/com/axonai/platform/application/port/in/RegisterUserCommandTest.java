
package com.axonai.platform.application.port.in;

import jakarta.validation.*;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserCommandTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar RegisterUserCommand com valores válidos")
    void deveCriarRegisterUserCommandValido() {
        char[] senha = "Senha@123".toCharArray();
        RegisterUserCommand cmd = new RegisterUserCommand("user@email.com", senha);

        assertEquals("user@email.com", cmd.email());
        assertArrayEquals(senha, cmd.password());
    }

    @Test
    @DisplayName("Deve validar e-mail vazio")
    void deveValidarEmailVazio() {
        RegisterUserCommand cmd = new RegisterUserCommand("", "Senha@123".toCharArray());
        Set<ConstraintViolation<RegisterUserCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Deve validar e-mail inválido")
    void deveValidarEmailInvalido() {
        RegisterUserCommand cmd = new RegisterUserCommand("email-invalido", "Senha@123".toCharArray());
        Set<ConstraintViolation<RegisterUserCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("Deve validar senha nula")
    void deveValidarSenhaNula() {
        RegisterUserCommand cmd = new RegisterUserCommand("user@email.com", null);
        Set<ConstraintViolation<RegisterUserCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Deve validar senha fraca")
    void deveValidarSenhaFraca() {
        // Sem maiúscula, minúscula, número ou caractere especial
        RegisterUserCommand cmd = new RegisterUserCommand("user@email.com", "senha".toCharArray());
        Set<ConstraintViolation<RegisterUserCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Deve validar senha com menos de 8 caracteres")
    void deveValidarSenhaCurta() {
        RegisterUserCommand cmd = new RegisterUserCommand("user@email.com", "S@1a".toCharArray());
        Set<ConstraintViolation<RegisterUserCommand>> violations = validator.validate(cmd);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    @DisplayName("Deve limpar a senha com clearPassword")
    void deveLimparSenhaComClearPassword() {
        char[] senha = "Senha@123".toCharArray();
        RegisterUserCommand cmd = new RegisterUserCommand("user@email.com", senha);

        cmd.clearPassword();

        for (char c : cmd.password()) {
            assertEquals('\0', c);
        }
    }
}