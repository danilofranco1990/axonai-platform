package com.axonai.platform.identity.application.port.out;

import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;

public interface JwtProvider {
    /**
     * Gera os tokens de acesso e de atualização para um determinado usuário.
     *
     * @param user O agregado do usuário para o qual os tokens serão gerados.
     * @return Um objeto JwtTokens contendo o access token, refresh token e tempo de expiração.
     */
    JwtTokens generateTokens(UserAggregate user);
}
