package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.*;
import com.ensiasit.projectx.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest);
    }

    @GetMapping("/current")
    public UserDto getCurrentUser(Principal principal) {
        return authService.getCurrentUser(principal.getName());
    }
}
