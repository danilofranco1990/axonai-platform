package com.axonai.platform.application.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Encapsula os dados necessários para executar o caso de uso de registro de usuário.
 * A validação de sintaxe e complexidade é aplicada na fronteira da camada de aplicação.
 * A validação semântica (ex: unicidade de e-mail) é responsabilidade do handler do comando.
 */
public record RegisterUserCommand(
        @NotBlank(message = "O e-mail não pode ser vazio.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @NotNull(message = "A senha não pode ser nula.")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial."
        )
        char[] password
) {

    public void clearPassword() {
        java.util.Arrays.fill(password, '\0');
    }
}