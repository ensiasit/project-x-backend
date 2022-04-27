package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.UserDto;
import com.ensiasit.projectx.services.UserService;
import com.ensiasit.projectx.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Api(tags = "/users")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation("Returns all users")
    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @ApiOperation("Returns a user")
    @GetMapping("/{id}")
    public UserDto getOne(@PathVariable long id) {
        return userService.getOne(id);
    }

    @ApiOperation("Creates a user")
    @PostMapping
    public UserDto addOne(@Valid @RequestBody UserDto payload) {
        return userService.addOne(payload);
    }

    @ApiOperation("Deletes a user")
    @DeleteMapping("/{id}")
    public UserDto deleteOne(@PathVariable long id) {
        return userService.deleteOne(id);
    }

    @ApiOperation("Updates a user")
    @PutMapping("/{id}")
    public UserDto updateOne(@PathVariable long id, @Valid @RequestBody UserDto payload) {
        return userService.updateOne(id, payload);
    }

    @ApiOperation("Updates the currently logged in user")
    @PutMapping
    public UserDto updateCurrent(Principal principal, @Valid @RequestBody UserDto userDto) {
        return userService.updateOne(principal.getName(), userDto);
    }
}
