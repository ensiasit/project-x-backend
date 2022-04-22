package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.services.UserService;
import com.ensiasit.projectx.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public UserDto addOne(Principal principal, @Valid @RequestBody UserDto payload) {
        return userService.addOne(principal.getName(), payload);
    }

    @DeleteMapping("/{id}")
    public UserDto deleteOne(Principal principal, @PathVariable long id) {
        return userService.deleteOne(principal.getName(), id);
    }

    @PutMapping("/{id}")
    public UserDto updateOne(Principal principal, @PathVariable long id, @Valid @RequestBody UserDto payload) {
        return userService.updateOne(principal.getName(), id, payload);
    }

    @PutMapping
    public UserDto updateCurrentUser(Principal principal, @Valid @RequestBody UserDto userDto) {
        return userService.updateByEmail(principal.getName(), userDto);
    }

    @GetMapping("/{id}")
    public UserDto getOne(Principal principal, @PathVariable long id) {
        return userService.getOneById(principal.getName(), id);
    }
}
