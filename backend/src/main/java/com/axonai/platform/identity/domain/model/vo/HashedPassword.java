package com.axonai.platform.identity.domain.model.vo;

import com.axonai.platform.identity.domain.exception.UserDomainException;
import java.util.Objects;
import java.util.regex.Pattern;

public record HashedPassword(String value) {

    private static final Pattern BCRYPT_PATTERN =
            Pattern.compile("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    public HashedPassword {
        Objects.requireNonNull(value, "O valor da senha com hash não pode ser nulo.");
        if (!BCRYPT_PATTERN.matcher(value).matches()) {
            throw new UserDomainException("Formato de hash de senha inválido.");
        }
    }

    @Override
    public String toString() {
        return "HashedPassword[value=REDACTED]";
    }


}
