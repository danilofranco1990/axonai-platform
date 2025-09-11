package com.axonai.platform.domain.model.vo;

import com.axonai.platform.domain.exception.InvalidEmailFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para o Value Object Email")
class EmailTest {

    @Test
    @DisplayName("Deve criar um Value Object Email a partir de uma string válida")
    void deveCriarEmailValido() {
        Email email = new Email("Exemplo.Teste@Dominio.com");
        assertEquals("exemplo.teste@dominio.com", email.value());
    }

    @Test
    @DisplayName("Deve lançar InvalidEmailFormatException para formatos de email inválidos")
    void deveLancarExcecaoParaEmailInvalido() {
        assertThrows(InvalidEmailFormatException.class, () -> new Email("email-invalido"));
        assertThrows(InvalidEmailFormatException.class, () -> new Email("usuario@dominio"));
        assertThrows(InvalidEmailFormatException.class, () -> new Email("usuario@.com"));
        assertThrows(InvalidEmailFormatException.class, () -> new Email("@dominio.com"));
        assertThrows(InvalidEmailFormatException.class, () -> new Email(" "));
    }

    @Test
    @DisplayName("Deve lançar NullPointerException para um email nulo")
    void deveLancarExcecaoParaEmailNulo() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    @DisplayName("Deve extrair a parte local do email corretamente")
    void deveExtrairLocalPartCorretamente() {
        Email email = new Email("usuario@dominio.com");
        assertEquals("usuario", email.getLocalPart());
    }

    @Test
    @DisplayName("Deve extrair o domínio do email corretamente")
    void deveExtrairDominioCorretamente() {
        Email email = new Email("usuario@dominio.com");
        assertEquals("dominio.com", email.getDomain());
    }

    @Test
    @DisplayName("Deve normalizar o valor do email para minúsculas durante a criação")
    void deveNormalizarEmailParaMinusculas() {
        Email email = new Email("UsUaRiO@DoMiNiO.CoM");
        assertEquals("usuario@dominio.com", email.value());
    }
}