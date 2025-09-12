package com.axonai.platform.application.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Encapsula os dados necessários para executar o caso de uso de registro de usuário.
 * A validação é aplicada aqui, na fronteira da camada de aplicação.
 */
public record RegisterUserCommand(
        @NotBlank(message = "O e-mail não pode ser vazio.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        String plainTextPassword
) {}