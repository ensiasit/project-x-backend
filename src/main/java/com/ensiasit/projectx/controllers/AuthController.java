package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.JwtResponse;
import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.RegisterRequest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        Pair<Optional<User>, String> response = authService.registerUser(registerRequest);

        if (response.getFirst().isEmpty()) {
            return ResponseEntity.badRequest().body(response.getSecond());
        } else {
            return ResponseEntity.ok(response.getSecond());
        }
    }
}
