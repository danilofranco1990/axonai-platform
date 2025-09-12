package com.axonai.platform.identity;

import com.axonai.platform.identity.application.port.out.JwtProvider;
import com.axonai.platform.identity.application.port.out.UserRepositoryPort;
import com.axonai.platform.identity.domain.service.PasswordPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class AxonaiPlatformApplicationTests extends PostgresContainerConfig {

    @MockitoBean private UserRepositoryPort userRepositoryPort;

    @MockitoBean private PasswordPolicy passwordPolicy;

    @MockitoBean private JwtProvider jwtProvider;

    @Test
    void contextLoads() {}
}
