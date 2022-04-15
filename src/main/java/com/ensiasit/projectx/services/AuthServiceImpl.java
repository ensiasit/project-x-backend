package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.*;
import com.ensiasit.projectx.exceptions.BadRequestException;
import com.ensiasit.projectx.exceptions.NotFoundException;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.models.UserContestRole;
import com.ensiasit.projectx.repositories.ContestRepository;
import com.ensiasit.projectx.repositories.UserContestRoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
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
    private final AdminService adminService;

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponse.builder()
                .token(jwt)
                .build();
    }

    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email already taken");
        }

        Optional<Contest> contest = contestRepository.findById(registerRequest.getContestId());

        if (contest.isEmpty()) {
            throw new BadRequestException("Incorrect contest id");
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

        return RegisterResponse.builder()
                .email(user.getEmail())
                .build();
    }

    @Override
    public UserDto getCurrentUser(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new NotFoundException("Incorrect user email");
        }

        return UserDto.builder()
                .email(user.get().getEmail())
                .username(user.get().getUsername())
                .password("HIDDEN")
                .isAdmin(adminService.isAdmin(userEmail))
                .build();
    }
}
