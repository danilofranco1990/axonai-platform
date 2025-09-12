package com.axonai.platform.application.port.in;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginCommand(
        @NotBlank(message = "O e-mail não pode ser vazio.")
                @Email(message = "O formato do e-mail é inválido.")
                String email,
        @NotNull(message = "A senha não pode ser nula.")
                @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
                char[] password) {
    public void clearPassword() {
        if (password != null) {
            java.util.Arrays.fill(password, '\0');
        }
    }
}
