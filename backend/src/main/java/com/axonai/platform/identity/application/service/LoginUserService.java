package com.axonai.platform.identity.application.service;

import com.axonai.platform.identity.application.port.in.AuthenticationResult;
import com.axonai.platform.identity.application.port.in.LoginCommand;
import com.axonai.platform.identity.application.port.in.LoginUseCase;
import com.axonai.platform.identity.application.port.out.JwtProvider;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.service.PasswordPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginUserService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordPolicy passwordPolicy;
    private final JwtProvider jwtProvider;

    public LoginUserService(
            UserRepositoryPort userRepositoryPort,
            PasswordPolicy passwordPolicy,
            JwtProvider jwtProvider) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordPolicy = passwordPolicy;
        this.jwtProvider = jwtProvider;
    }

    @Transactional(readOnly = true) // Operação de login é apenas de leitura
    @Override
    public AuthenticationResult login(LoginCommand command) {
        // Lógica de orquestração do login será implementada aqui.
        return null; // Placeholder
    }
}