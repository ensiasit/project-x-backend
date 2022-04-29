package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.LoginRequestDto;
import com.ensiasit.projectx.dto.LoginResponseDto;
import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.services.AuthenticationService;
import com.ensiasit.projectx.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.ensiasit.projectx.utils.Constants.API_PREFIX;

@Api(tags = "/auth")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(API_PREFIX + "/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserService userService;

    @ApiOperation("Authenticates a user using email and password")
    @PostMapping("/login")
    public LoginResponseDto authenticate(@Valid @RequestBody LoginRequestDto loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @ApiOperation("Registers a new user")
    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserDto payload) {
        return userService.addOne(payload);
    }

    @ApiOperation("Returns information about the currently logged in user")
    @GetMapping("/current")
    public UserDto getCurrent(Principal principal) {
        return userService.getOne(principal.getName());
    }
}
