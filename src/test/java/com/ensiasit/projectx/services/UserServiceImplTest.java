package com.ensiasit.projectx.services;

import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.repositories.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private UserServiceImpl userService;
}