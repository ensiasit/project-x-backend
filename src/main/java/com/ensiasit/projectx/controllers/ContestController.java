package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.services.ContestService;
import com.ensiasit.projectx.utils.Constants;
import com.ensiasit.projectx.utils.RoleEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Api(tags = "/contests")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/contests")
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;

    @ApiOperation("Returns all contests")
    @GetMapping
    public List<ContestDto> getAll() {
        return contestService.getAll();
    }

    @ApiOperation("Returns a contest")
    @GetMapping("/{id}")
    public ContestDto getOne(@PathVariable long id) {
        return contestService.getOne(id);
    }

    @ApiOperation("Creates a contest")
    @PostMapping
    public ContestDto addOne(@Valid @RequestBody ContestDto contest) {
        return contestService.addOne(contest);
    }

    @ApiOperation("Deletes a contest")
    @DeleteMapping("/{id}")
    public ContestDto deleteOne(@PathVariable long id) {
        return contestService.deleteOne(id);
    }

    @ApiOperation("Updates a contest")
    @PutMapping("/{id}")
    public ContestDto updateOne(@PathVariable Long id, @Valid @RequestBody ContestDto payload) {
        return contestService.updateOne(id, payload);
    }

    @ApiOperation("Returns all users roles for a contest")
    @GetMapping("/{id}/roles")
    public List<UserContestRoleDto> getAllRoles(@PathVariable long id) {
        return contestService.getAllRoles(id);
    }

    @ApiOperation("Returns all contests roles for the currently logged in user")
    @GetMapping("/roles")
    public List<UserContestRoleDto> getAllRolesForCurrentUser(Principal principal) {
        return contestService.getAllRolesByUserEmail(principal.getName());
    }

    @ApiOperation("Updates the role of a user in a contest")
    @PutMapping("/{contestId}/roles/users/{userId}/{role}")
    public UserContestRoleDto updateRole(Principal principal, @PathVariable long contestId, @PathVariable long userId, @PathVariable RoleEnum role) {
        return contestService.updateRole(principal.getName(), contestId, userId, role);
    }
}
