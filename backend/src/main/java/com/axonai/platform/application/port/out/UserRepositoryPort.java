package com.axonai.platform.application.port.out;

import com.axonai.platform.domain.model.aggregate.UserAggregate;
import com.axonai.platform.domain.model.vo.Email;
import com.axonai.platform.domain.model.vo.UserId; // Assumindo a existência de um VO para o ID
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
