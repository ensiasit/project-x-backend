package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.request.LoginRequest;
import com.ensiasit.projectx.dto.request.RegisterRequest;
import com.ensiasit.projectx.dto.response.JwtResponse;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.AbstractMap;
import java.util.Optional;

import static com.ensiasit.projectx.utils.Constants.API_PREFIX;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(API_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        AbstractMap.SimpleEntry<Optional<User>, String> response = authService.registerUser(registerRequest);
        if (response.getKey().isEmpty()) {
            return ResponseEntity.badRequest().body(response.getValue());
        } else {
            return ResponseEntity.ok(response.getValue());
        }
    }
}
