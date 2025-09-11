package com.axonai.platform.domain.service;

import com.axonai.platform.domain.model.vo.HashedPassword;

public interface PasswordPolicy {
    HashedPassword hash(String rawPassword);
    boolean verify(String rawPassword, HashedPassword hashedPassword);
}