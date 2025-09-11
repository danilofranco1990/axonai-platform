package com.axonai.platform.domain.model.vo;

import com.axonai.platform.domain.exception.UserDomainException;
import java.util.Objects;
import java.util.regex.Pattern;

public record HashedPassword(String value) {

    // Pré-compilar o Pattern é mais performático que usar String.matches() repetidamente.
    private static final Pattern BCRYPT_PATTERN =
            Pattern.compile("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    public HashedPassword {
        Objects.requireNonNull(value, "O valor da senha com hash não pode ser nulo.");
        if (!BCRYPT_PATTERN.matcher(value).matches()) {
            // A mensagem de exceção não deve vazar detalhes do formato esperado.
            throw new UserDomainException("Formato de hash de senha inválido.");
        }
    }

    /**
     * Sobrescreve o método toString() para evitar o vazamento do hash em logs ou outras
     * representações de string.
     *
     * @return Uma representação redigida do objeto.
     */
    @Override
    public String toString() {
        return "HashedPassword[value=REDACTED]";
    }
}
