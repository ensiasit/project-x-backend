package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.LoginResponse;
import com.ensiasit.projectx.dto.RegisterRequest;

public interface AuthService {
    LoginResponse authenticateUser(LoginRequest loginRequest);

    void registerUser(RegisterRequest registerRequest);
}
