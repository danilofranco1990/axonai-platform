package com.axonai.platform.application.port.in;

import com.axonai.platform.application.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Encapsula os dados necessários para executar o caso de uso de registro de usuário. A validação de
 * sintaxe e complexidade é aplicada na fronteira da camada de aplicação. A validação semântica (ex:
 * unicidade de e-mail) é responsabilidade do handler do comando.
 */
public record RegisterUserCommand(
        @NotBlank(message = "O e-mail não pode ser vazio.")
        @Email(message = "Formato de e-mail inválido.")
        String email,

        @NotNull(message = "A senha não pode ser nula.")
        @Password
        char[] password
) {

    public void clearPassword() {
        java.util.Arrays.fill(password, '\0');
    }
}
