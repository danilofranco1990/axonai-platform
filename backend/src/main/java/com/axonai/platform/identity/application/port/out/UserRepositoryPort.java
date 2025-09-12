package com.axonai.platform.identity.application.port.out;

import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;
import com.axonai.platform.identity.domain.model.vo.Email;
import com.axonai.platform.identity.domain.model.vo.UserId;
import java.util.Optional;

/**
 * Porta de Saída (Output Port) que define o contrato para operações de persistência com o agregado
 * de Usuário. As operações são intencionalmente explícitas.
 */
public interface UserRepositoryPort {

    UserAggregate save(UserAggregate user);

    Optional<UserAggregate> findById(UserId id);

    Optional<UserAggregate> findByEmail(Email email);

    boolean existsByEmail(Email email);

    void deleteById(UserId id);
}
