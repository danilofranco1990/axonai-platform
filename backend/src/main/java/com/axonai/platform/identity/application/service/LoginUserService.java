package com.axonai.platform.identity.application.service;

import com.axonai.platform.identity.application.exception.AuthenticationFailureException;
import com.axonai.platform.identity.application.port.in.AuthenticationResult;
import com.axonai.platform.identity.application.port.in.LoginCommand;
import com.axonai.platform.identity.application.port.in.LoginUseCase;
import com.axonai.platform.identity.application.port.out.JwtProvider;
import com.axonai.platform.identity.application.port.out.JwtTokens;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;
import com.axonai.platform.identity.domain.model.vo.Email;
import com.axonai.platform.identity.domain.model.vo.HashedPassword;
import com.axonai.platform.identity.domain.service.PasswordPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class LoginUserService implements LoginUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordPolicy passwordPolicy;
    private final JwtProvider jwtProvider;

    private static final HashedPassword DUMMY_HASH = new HashedPassword("$2a$10$9.zL4s2K2b3m4n5p6q7r8s9t0u1v2w3x4y5z6a7b8c9d0e1f2g3h4");
    private static final String AUTH_FAILURE_MESSAGE = "E-mail ou senha inválidos.";

    public LoginUserService(
            UserRepositoryPort userRepositoryPort,
            PasswordPolicy passwordPolicy,
            JwtProvider jwtProvider) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordPolicy = passwordPolicy;
        this.jwtProvider = jwtProvider;
    }

    @Transactional(readOnly = true)
    @Override
    public AuthenticationResult login(LoginCommand command) {
        try {
            final var email = new Email(command.email());
            Optional<UserAggregate> userOptional = userRepositoryPort.findByEmail(email);

            HashedPassword passwordToVerify = userOptional
                    .map(UserAggregate::getHashedPassword)
                    .orElse(DUMMY_HASH);

            boolean passwordMatches = passwordPolicy.verify(new String(command.password()), passwordToVerify);

            if (userOptional.isEmpty() || !passwordMatches) {
                throw new AuthenticationFailureException(AUTH_FAILURE_MESSAGE);
            }

            UserAggregate user = userOptional.get();

            user.ensureIsActive();

            JwtTokens tokens = jwtProvider.generateTokens(user);
            return new AuthenticationResult(
                    tokens.accessToken(),
                    tokens.expiresIn(),
                    tokens.refreshToken(),
                    "Bearer",
                    new AuthenticationResult.UserInfo(
                            user.getUserId().value().toString(),
                            user.getEmail().value(),
                            null
                    )
            );

        } catch (Exception ex) {
            if (ex instanceof AuthenticationFailureException) {
                throw ex;
            }
            throw new AuthenticationFailureException(AUTH_FAILURE_MESSAGE);
        } finally {
            command.clearPassword();
        }
    }
}