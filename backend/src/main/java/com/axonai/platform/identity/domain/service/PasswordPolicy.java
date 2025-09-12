package com.axonai.platform.identity.domain.service;

import com.axonai.platform.identity.domain.model.vo.HashedPassword;

public interface PasswordPolicy {
    HashedPassword hash(String rawPassword);

    boolean verify(String rawPassword, HashedPassword hashedPassword);
}
