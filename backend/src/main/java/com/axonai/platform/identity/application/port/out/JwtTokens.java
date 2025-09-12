package com.axonai.platform.identity.application.port.out;

/** DTO para encapsular os tokens gerados pelo JwtProvider. */
public record JwtTokens(String accessToken, String refreshToken, long expiresIn) {}
