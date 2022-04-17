package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.JwtResponse;
import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.RegisterRequest;
import com.ensiasit.projectx.models.User;
import org.springframework.data.util.Pair;

import java.util.Optional;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);

    Pair<Optional<User>, String> registerUser(RegisterRequest registerRequest);
}
