package com.axonai.platform.identity.application.service;

import com.axonai.platform.identity.application.exception.AuthenticationFailureException;
import com.axonai.platform.identity.application.port.in.AuthenticationResult;
import com.axonai.platform.identity.application.port.in.LoginCommand;
import com.axonai.platform.identity.application.port.in.LoginUseCase;
import com.axonai.platform.identity.application.port.out.JwtProvider;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.exception.UserDomainException;
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
        try {
            final var email = new Email(command.email());

            UserAggregate user = userRepositoryPort.findByEmail(email)
                    .orElseThrow(() -> new AuthenticationFailureException(AUTH_FAILURE_MESSAGE));

            try {
                user.ensureIsActive();
            } catch (UserDomainException ex) {
                throw new AuthenticationFailureException(AUTH_FAILURE_MESSAGE);
            }

            // 1. Delegar a verificação da senha para o serviço de domínio
            boolean passwordMatches = passwordPolicy.verify(
                    new String(command.password()),
                    user.getHashedPassword()
            );

            // 2. Se a senha não corresponder, lançar a exceção de falha
            if (!passwordMatches) {
                throw new AuthenticationFailureException(AUTH_FAILURE_MESSAGE);
            }

            // TODO: Próximo passo: gerar o token.
            return null; // Placeholder

        } finally {
            // 3. Garantir que a senha seja limpa da memória em todos os cenários
            command.clearPassword();
        }
    }
}