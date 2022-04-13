package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.request.LoginRequest;
import com.ensiasit.projectx.dto.request.RegisterRequest;
import com.ensiasit.projectx.dto.request.TokenRefreshRequest;
import com.ensiasit.projectx.dto.response.JwtResponse;
import com.ensiasit.projectx.dto.response.TokenRefreshResponse;
import com.ensiasit.projectx.exception.TokenRefreshException;
import com.ensiasit.projectx.models.RefreshToken;
import com.ensiasit.projectx.models.Role;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.RoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.security.jwt.JwtUtils;
import com.ensiasit.projectx.security.services.RefreshTokenService;
import com.ensiasit.projectx.security.services.UserDetailsImpl;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        Set<RoleEnum> roles = userDetails.getAuthorities().stream()
                .map(item -> RoleEnum.valueOf(item.getAuthority()))
                .collect(Collectors.toSet());

        return JwtResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public AbstractMap.SimpleEntry<Optional<User>, String> registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new AbstractMap.SimpleEntry<>(Optional.empty(), "Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new AbstractMap.SimpleEntry<>(Optional.empty(), "Error: Email is already taken!");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .build();

        Set<String> strRequestedRoles = registerRequest.getRoles();
        strRequestedRoles.removeIf(strRole -> !contains(strRole));
        Set<RoleEnum> requestedRoles = strRequestedRoles.stream().map(RoleEnum::valueOf).collect(Collectors.toSet());
        Set<Role> roles = new HashSet<>();

        if (requestedRoles.isEmpty()) {
            if (roleRepository.findByName(RoleEnum.ROLE_USER).isPresent()) {
                Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER).get();
                roles.add(userRole);
            } else {
                return new AbstractMap.SimpleEntry<>(Optional.empty(), "No role exists for user, create it and try again!");
            }
        } else {
            for (RoleEnum role : requestedRoles) {
                if (roleRepository.findByName(role).isPresent()) {
                    Role roleItem = roleRepository.findByName(role).get();
                    roles.add(roleItem);
                } else {
                    return new AbstractMap.SimpleEntry<>(Optional.empty(), "One/many of the roles specified doesn't exist, create it/them and try again!");
                }
            }
        }
        user.setRoles(roles);
        userRepository.save(user);

        return new AbstractMap.SimpleEntry<>(Optional.of(user), "User registered successfully!");
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();
        if (refreshTokenService.findByToken(requestRefreshToken).isPresent()) {
            RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken).get();
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
            User user = refreshToken.getUser();
            String token = jwtUtils.generateTokenFromUsername(user.getUsername());

            return TokenRefreshResponse.builder()
                    .accessToken(token)
                    .refreshToken(requestRefreshToken)
                    .build();
        } else {
            throw new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!");
        }
    }

    private boolean contains(String strRole) {
        for (RoleEnum enumRole : RoleEnum.values()) {
            if (enumRole.name().equals(strRole)) {
                return true;
            }
        }

        return false;
    }
}
