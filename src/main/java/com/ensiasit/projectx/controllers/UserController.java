package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.services.UserService;
import com.ensiasit.projectx.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping
    public UserDto updateCurrentUser(Principal principal, @Valid @RequestBody UserDto userDto) {
        return userService.updateByEmail(principal.getName(), userDto);
    }
}
