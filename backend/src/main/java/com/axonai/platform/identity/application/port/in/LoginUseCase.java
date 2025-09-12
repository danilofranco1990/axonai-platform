package com.axonai.platform.identity.application.port.in;

public interface LoginUseCase {

    AuthenticationResult login(LoginCommand command);
}
