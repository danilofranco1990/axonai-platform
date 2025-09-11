package com.axonai.platform.domain.model.vo;

import com.axonai.platform.domain.exception.InvalidEmailFormatException;
import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    // Ele valida a estrutura de nomes de domínio e a composição da parte local de forma mais
    // estrita.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public Email(String value) {
        // 1. Canonicalização: O valor é normalizado para minúsculas.
        String canonicalEmail =
                Objects.requireNonNull(value, "Email value cannot be null.").trim().toLowerCase();

        // 2. Validação: A verificação é feita no valor canônico.
        if (canonicalEmail.isBlank() || !EMAIL_PATTERN.matcher(canonicalEmail).matches()) {
            throw new InvalidEmailFormatException("Invalid email format: " + value);
        }

        // 3. Atribuição: O valor canônico (e não o original) é atribuído ao campo do record.
        this.value = canonicalEmail;
    }

    /** Extrai a parte local do e-mail (o que vem antes do @). */
    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }

    /** Extrai o domínio do e-mail (o que vem depois do @). */
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }
}
