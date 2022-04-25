package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.LoginRequest;
import com.ensiasit.projectx.dto.LoginResponse;
import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.exceptions.BadRequestException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void given_login_request_authenticate_user_when_authenticateUser() {
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
    void given_not_taken_email_should_register_user_when_registerUser() {
        UserDto dto = UserDto.builder()
                .email("email")
                .build();
        User user = mock(User.class);

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.fromDto(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto userDto = authService.registerUser(dto);

        verify(userMapper).fromDto(dto);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);

        assertThat(userDto).isEqualTo(dto);
    }

    @Test
    void given_taken_email_should_throw_exception_when_registerUser() {
        UserDto dto = UserDto.builder()
                .email("email")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> authService.registerUser(dto)
        );
    }
}