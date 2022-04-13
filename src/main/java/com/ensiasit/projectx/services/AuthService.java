package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.request.LoginRequest;
import com.ensiasit.projectx.dto.request.RegisterRequest;
import com.ensiasit.projectx.dto.request.TokenRefreshRequest;
import com.ensiasit.projectx.dto.response.JwtResponse;
import com.ensiasit.projectx.dto.response.TokenRefreshResponse;
import com.ensiasit.projectx.models.User;

import java.util.AbstractMap;
import java.util.Optional;

public interface AuthService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    AbstractMap.SimpleEntry<Optional<User>, String> registerUser(RegisterRequest registerRequest);

    TokenRefreshResponse refreshToken(TokenRefreshRequest tokenRefreshRequest);
}
