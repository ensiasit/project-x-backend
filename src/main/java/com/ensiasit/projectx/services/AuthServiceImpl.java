package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.JwtResponse;
import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.RegisterRequest;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.security.jwt.JwtUtils;
import com.ensiasit.projectx.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final ContestRepository contestRepository;
    private final UserContestRoleRepository userContestRoleRepository;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return JwtResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .build();
    }

    @Override
    public Pair<Optional<User>, String> registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return Pair.of(Optional.empty(), "Error: Email is already taken!");
        }

        Optional<Contest> contest = contestRepository.findById(registerRequest.getContestId());

        if (contest.isEmpty()) {
            return Pair.of(Optional.empty(), "Error: Invalid contest id!");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .build();

        user = userRepository.save(user);

        UserContestRole userContestRole = UserContestRole.builder()
                .user(user)
                .contest(contest.get())
                .role(registerRequest.getRole())
                .build();

        userContestRoleRepository.save(userContestRole);

        return Pair.of(Optional.of(user), "User registered successfully!");
    }
}
