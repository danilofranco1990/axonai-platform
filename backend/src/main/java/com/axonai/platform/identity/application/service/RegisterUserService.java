package com.axonai.platform.identity.application.service;

import com.axonai.platform.identity.application.exception.ConflictException;
import com.axonai.platform.identity.application.port.in.RegisterUserCommand;
import com.axonai.platform.identity.application.port.in.RegisterUserUseCase;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.service.PasswordPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.axonai.platform.identity.domain.model.vo.Email;
/**
 * Serviço de aplicação que orquestra o caso de uso de registro de um novo usuário.
 */
@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordPolicy passwordPolicy;

    public RegisterUserService(UserRepositoryPort userRepositoryPort, PasswordPolicy passwordPolicy) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordPolicy = passwordPolicy;
    }

    @Transactional
    @Override
    public void registerUser(RegisterUserCommand command) {
        final var email = new Email(command.email());

        if (userRepositoryPort.existsByEmail(email)) {
            throw new ConflictException("O e-mail '%s' já está em uso.".formatted(email.value()));
        }

        // TODO: Próximos passos da orquestração
    }
}