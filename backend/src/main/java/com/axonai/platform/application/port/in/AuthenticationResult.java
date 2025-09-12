package com.axonai.platform.application.port.in;

public record AuthenticationResult(
        String accessToken, long expiresIn, String refreshToken, String tokenType, UserInfo user) {

    public record UserInfo(String id, String email, String name) {}
}
