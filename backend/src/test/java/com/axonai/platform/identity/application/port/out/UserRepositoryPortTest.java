package com.axonai.platform.identity.application.port.out;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;
import com.axonai.platform.identity.domain.model.vo.Email;
import com.axonai.platform.identity.domain.model.vo.UserId;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRepositoryPortTest {

    @Test
    @DisplayName("Deve salvar usuário")
    void deveSalvarUsuario() {
        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        UserAggregate user = mock(UserAggregate.class);

        when(repo.save(user)).thenReturn(user);

        UserAggregate salvo = repo.save(user);

        assertEquals(user, salvo);
        verify(repo).save(user);
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarPorId() {
        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        UserId id = mock(UserId.class);
        UserAggregate user = mock(UserAggregate.class);

        when(repo.findById(id)).thenReturn(Optional.of(user));

        Optional<UserAggregate> result = repo.findById(id);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(repo).findById(id);
    }

    @Test
    @DisplayName("Deve buscar usuário por e-mail")
    void deveBuscarPorEmail() {
        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        Email email = mock(Email.class);
        UserAggregate user = mock(UserAggregate.class);

        when(repo.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<UserAggregate> result = repo.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(repo).findByEmail(email);
    }

    @Test
    @DisplayName("Deve verificar existência por e-mail")
    void deveVerificarExistenciaPorEmail() {
        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        Email email = mock(Email.class);

        when(repo.existsByEmail(email)).thenReturn(true);

        assertTrue(repo.existsByEmail(email));
        verify(repo).existsByEmail(email);
    }

    @Test
    @DisplayName("Deve deletar usuário por ID")
    void deveDeletarPorId() {
        UserRepositoryPort repo = mock(UserRepositoryPort.class);
        UserId id = mock(UserId.class);

        doNothing().when(repo).deleteById(id);

        repo.deleteById(id);

        verify(repo).deleteById(id);
    }
}
