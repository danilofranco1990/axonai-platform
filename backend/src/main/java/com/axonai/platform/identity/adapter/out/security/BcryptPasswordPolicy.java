package com.axonai.platform.identity.adapter.out.security;

import com.axonai.platform.identity.domain.model.vo.HashedPassword;
import com.axonai.platform.identity.domain.service.PasswordPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adaptador que implementa a porta PasswordPolicy utilizando a biblioteca Spring Security com o
 * algoritmo Bcrypt.
 */
@Component
public class BcryptPasswordPolicy implements PasswordPolicy {

    private final PasswordEncoder passwordEncoder;

    public BcryptPasswordPolicy(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public HashedPassword hash(String rawPassword) {
        String encodedPassword = this.passwordEncoder.encode(rawPassword);
        return new HashedPassword(encodedPassword);
    }

    @Override
    public boolean verify(String rawPassword, HashedPassword hashedPassword) {
        return this.passwordEncoder.matches(rawPassword, hashedPassword.value());
    }
}
