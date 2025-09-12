package com.axonai.platform.application.port.in;

import com.axonai.platform.application.port.in.AuthenticationResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationResultTest {

    @Test
    @DisplayName("Deve criar AuthenticationResult com valores corretos")
    void deveCriarAuthenticationResultComValoresCorretos() {
        AuthenticationResult.UserInfo userInfo = new AuthenticationResult.UserInfo("1", "user@email.com", "User Name");
        AuthenticationResult result = new AuthenticationResult("token123", 3600L, "refresh456", "Bearer", userInfo);

        assertEquals("token123", result.accessToken());
        assertEquals(3600L, result.expiresIn());
        assertEquals("refresh456", result.refreshToken());
        assertEquals("Bearer", result.tokenType());
        assertEquals(userInfo, result.user());
    }

    @Test
    @DisplayName("Deve criar UserInfo com valores corretos")
    void deveCriarUserInfoComValoresCorretos() {
        AuthenticationResult.UserInfo userInfo = new AuthenticationResult.UserInfo("2", "test@domain.com", "Test User");

        assertEquals("2", userInfo.id());
        assertEquals("test@domain.com", userInfo.email());
        assertEquals("Test User", userInfo.name());
    }

    @Test
    @DisplayName("Deve testar igualdade entre AuthenticationResults")
    void deveTestarIgualdadeEntreAuthenticationResults() {
        AuthenticationResult.UserInfo userInfo1 = new AuthenticationResult.UserInfo("1", "a@b.com", "A");
        AuthenticationResult.UserInfo userInfo2 = new AuthenticationResult.UserInfo("1", "a@b.com", "A");
        AuthenticationResult result1 = new AuthenticationResult("t", 1L, "r", "type", userInfo1);
        AuthenticationResult result2 = new AuthenticationResult("t", 1L, "r", "type", userInfo2);

        assertEquals(result1, result2);
        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    @DisplayName("Deve permitir campos nulos")
    void devePermitirCamposNulos() {
        AuthenticationResult.UserInfo userInfo = new AuthenticationResult.UserInfo(null, null, null);
        AuthenticationResult result = new AuthenticationResult(null, 0L, null, null, userInfo);

        assertNull(result.accessToken());
        assertEquals(0L, result.expiresIn());
        assertNull(result.refreshToken());
        assertNull(result.tokenType());
        assertNotNull(result.user());
        assertNull(result.user().id());
        assertNull(result.user().email());
        assertNull(result.user().name());
    }
}