package com.axonai.platform.application.port.in;

public interface LoginUseCase {

    AuthenticationResult login(LoginCommand command);
}