package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.LoginResponse;
import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.services.AuthService;
import com.ensiasit.projectx.services.UserService;
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
    private final UserService userService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public UserDto registerUser(@Valid @RequestBody UserDto payload) {
        return authService.registerUser(payload);
    }

    @GetMapping("/current")
    public UserDto getCurrentUser(Principal principal) {
        return userService.findByEmail(principal.getName());
    }
}
