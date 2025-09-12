package com.axonai.platform.identity.application.service;

import com.axonai.platform.identity.application.exception.AuthenticationFailureException;
import com.axonai.platform.identity.application.port.in.AuthenticationResult;
import com.axonai.platform.identity.application.port.in.LoginCommand;
import com.axonai.platform.identity.application.port.in.LoginUseCase;
import com.axonai.platform.identity.application.port.out.JwtProvider;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;
import com.axonai.platform.identity.domain.model.vo.Email;
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

    private static final String AUTH_FAILURE_MESSAGE = "E-mail ou senha inválidos.";

    @Transactional(readOnly = true)
    @Override
    public AuthenticationResult login(LoginCommand command) {
        final var email = new Email(command.email());

        // 1. Buscar o usuário pelo e-mail usando a porta do repositório
        UserAggregate user = userRepositoryPort.findByEmail(email)
                // 2. Se não encontrar, lançar a exceção de falha de autenticação
                .orElseThrow(() -> new AuthenticationFailureException(AUTH_FAILURE_MESSAGE));

        // TODO: Próximos passos: verificar status, verificar senha, gerar token.
        return null; // Placeholder
    }
}