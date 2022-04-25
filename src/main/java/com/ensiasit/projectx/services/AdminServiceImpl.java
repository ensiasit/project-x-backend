package com.ensiasit.projectx.services;

import com.ensiasit.projectx.models.Admin;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.AdminRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.ensiasit.projectx.utils.Constants.DEFAULT_ADMIN_USERNAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Value("${projectx.app.adminEmail}")
    private String adminEmail;

    @Value("${projectx.app.adminPassword}")
    private String adminPassword;

    private final PasswordEncoder encoder;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @PostConstruct
    @Override
    public void createAdmin() {
        boolean adminExists = adminRepository.count() >= 1;

        if (adminExists) {
            log.info("Admin user already exists.");
        } else {
            User adminUser = User.builder()
                    .username(DEFAULT_ADMIN_USERNAME)
                    .email(adminEmail)
                    .password(encoder.encode(adminPassword))
                    .build();

            adminUser = userRepository.save(adminUser);

            Admin admin = Admin.builder()
                    .user(adminUser)
                    .build();

            adminRepository.save(admin);

            log.info("Admin user created.");
        }
    }

    @Override
    public boolean isAdmin(String userEmail) {
        return adminRepository.existsByUserEmail(userEmail);
    }

    @Override
    public User getAdmin() {
        return adminRepository.findAll().get(0).getUser();
    }
}
