package com.ensiasit.projectx.services;

import com.ensiasit.projectx.models.Admin;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.AdminRepository;
import com.ensiasit.projectx.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static com.ensiasit.projectx.utils.Constants.DEFAULT_ADMIN_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
    private final String adminEmail = "adminEmail";
    private final String adminPassword = "adminPassword";

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(adminService, "adminEmail", adminEmail);
        ReflectionTestUtils.setField(adminService, "adminPassword", adminPassword);
    }

    @Test
    void given_no_admin_should_create_admin_when_createAdmin() {
        User adminUser = User.builder()
                .id(1L)
                .email(adminEmail)
                .username("username")
                .password(adminPassword)
                .build();
        String encodedPassword = "encodedPassword";

        when(adminRepository.count()).thenReturn(0L);
        when(userRepository.save(any(User.class))).thenReturn(adminUser);
        when(adminRepository.save(any(Admin.class))).thenReturn(null);
        when(encoder.encode(adminPassword)).thenReturn(encodedPassword);

        adminService.createAdmin();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Admin> adminCaptor = ArgumentCaptor.forClass(Admin.class);

        verify(userRepository).save(userCaptor.capture());
        verify(adminRepository).save(adminCaptor.capture());

        User user = userCaptor.getValue();
        Admin admin = adminCaptor.getValue();

        assertThat(user.getUsername()).isEqualTo(DEFAULT_ADMIN_USERNAME);
        assertThat(user.getEmail()).isEqualTo(adminEmail);
        assertThat(user.getPassword()).isEqualTo(encodedPassword);

        assertThat(admin.getUser().getId()).isEqualTo(adminUser.getId());
        assertThat(admin.getUser().getEmail()).isEqualTo(adminUser.getEmail());
        assertThat(admin.getUser().getUsername()).isEqualTo(adminUser.getUsername());
        assertThat(admin.getUser().getPassword()).isEqualTo(adminUser.getPassword());
    }

    @Test
    void given_admin_should_not_create_admin_when_createAdmin() {
        when(adminRepository.count()).thenReturn(1L);

        adminService.createAdmin();

        verifyNoMoreInteractions(adminRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    void when_isAdmin() {
        String email = "email";
        boolean exists = true;

        when(adminRepository.existsByUserEmail(email)).thenReturn(exists);

        boolean isAdmin = adminService.isAdmin(email);

        verify(adminRepository).existsByUserEmail(email);

        assertThat(isAdmin).isEqualTo(exists);
    }

    @Test
    void when_getAdmin() {
        User userAdmin = mock(User.class);
        List<Admin> admins = List.of(Admin.builder()
                .user(userAdmin)
                .build());

        when(adminRepository.findAll()).thenReturn(admins);

        User user = adminService.getAdmin();

        verify(adminRepository).findAll();

        assertThat(user).isEqualTo(userAdmin);
    }
}