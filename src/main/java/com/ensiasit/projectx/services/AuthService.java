package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.LoginResponse;
import com.ensiasit.projectx.dto.RegisterRequest;
import com.ensiasit.projectx.dto.RegisterResponse;

public interface AuthService {
    LoginResponse authenticateUser(LoginRequest loginRequest);

    RegisterResponse registerUser(RegisterRequest registerRequest);
}
