package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.LoginResponse;
import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.mappers.UserMapper;
import com.ensiasit.projectx.models.User;
import com.ensiasit.projectx.repositories.UserRepository;
import com.ensiasit.projectx.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AdminService adminService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authenticateUser() {
        Authentication authentication = mock(Authentication.class);
        String token = "token";
        LoginRequest request = LoginRequest.builder()
                .email("email")
                .password("password")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        LoginResponse response = authService.authenticateUser(request);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(authenticationManager).authenticate(authenticationCaptor.capture());
        verify(jwtUtils).generateJwtToken(authentication);

        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = authenticationCaptor.getValue();

        assertThat(usernamePasswordAuthentication.getPrincipal()).isEqualTo(request.getEmail());
        assertThat(usernamePasswordAuthentication.getCredentials()).isEqualTo(request.getPassword());

        assertThat(response.getToken()).isEqualTo(token);
    }

    @Test
    void registerUser() {
        UserDto dto = mock(UserDto.class);
        User adminUser = User.builder().email("email").build();

        when(adminService.getAdmin()).thenReturn(adminUser);
        when(userService.addOne(adminUser.getEmail(), dto)).thenReturn(dto);

        UserDto userDto = authService.registerUser(dto);

        assertThat(userDto).isEqualTo(dto);
    }
}