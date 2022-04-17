package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.*;

public interface AuthService {
    LoginResponse authenticateUser(LoginRequest loginRequest);

    RegisterResponse registerUser(RegisterRequest registerRequest);

    UserDto getCurrentUser(String userEmail);
}
