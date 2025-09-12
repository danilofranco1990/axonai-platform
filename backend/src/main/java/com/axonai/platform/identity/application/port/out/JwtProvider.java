package com.axonai.platform.identity.application.port.out;

import com.axonai.platform.identity.domain.model.aggregate.UserAggregate;


public interface JwtProvider {

    String generateAccessToken(UserAggregate user);
}