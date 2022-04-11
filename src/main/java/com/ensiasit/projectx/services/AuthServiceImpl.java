package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.request.RegisterRequest;
import com.ensiasit.projectx.models.Role;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.RoleRepository;
import com.ensiasit.projectx.utils.RoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(RoleRepository roleRepository, PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public User registerUser(RegisterRequest registerRequest) {
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword())
        );
        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);

        return user;
    }
}
