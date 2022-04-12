package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.request.LoginRequest;
import com.ensiasit.projectx.dto.request.RegisterRequest;
import com.ensiasit.projectx.dto.response.JwtResponse;
import com.ensiasit.projectx.models.Role;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.RoleRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.security.jwt.JwtUtils;
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
        Set<RoleEnum> roles = userDetails.getAuthorities().stream()
                .map(item -> RoleEnum.valueOf(item.getAuthority()))
                .collect(Collectors.toSet());

        return JwtResponse.builder()
                .accessToken(jwt)
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
        Set<RoleEnum> requestedRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (requestedRoles == null || requestedRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER).get();
            roles.add(userRole);
        } else {
            for (RoleEnum role : requestedRoles) {
                if (roleRepository.findByName(role).isPresent()) {
                    Role roleItem = roleRepository.findByName(role).get();
                    roles.add(roleItem);
                } else {
                    return new AbstractMap.SimpleEntry<>(Optional.empty(), "Role not found!");
                }
            }
        }
        user.setRoles(roles);
        userRepository.save(user);

        return new AbstractMap.SimpleEntry<>(Optional.of(user), "User registered successfully!");
    }
}
