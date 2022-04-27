package com.ensiasit.projectx.services;

import com.ensiasit.projectx.dto.LoginRequestDto;
import com.ensiasit.projectx.dto.LoginResponseDto;
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


    @InjectMocks
    private AuthenticationServiceImpl authService;

    @Test
    void authenticateUser() {
        Authentication authentication = mock(Authentication.class);
        String token = "token";
        LoginRequestDto request = LoginRequestDto.builder()
                .email("email")
                .password("password")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

        LoginResponseDto response = authService.authenticate(request);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        verify(authenticationManager).authenticate(authenticationCaptor.capture());
        verify(jwtUtils).generateJwtToken(authentication);

        UsernamePasswordAuthenticationToken usernamePasswordAuthentication = authenticationCaptor.getValue();

        assertThat(usernamePasswordAuthentication.getPrincipal()).isEqualTo(request.getEmail());
        assertThat(usernamePasswordAuthentication.getCredentials()).isEqualTo(request.getPassword());

        assertThat(response.getToken()).isEqualTo(token);
    }
}