package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.LoginResponse;
import com.ensiasit.projectx.dto.RegisterRequest;
import com.ensiasit.projectx.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.ensiasit.projectx.utils.Constants.API_PREFIX;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(API_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);
    }
}
