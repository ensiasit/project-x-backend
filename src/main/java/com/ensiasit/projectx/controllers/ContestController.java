package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.services.AdminService;
import com.ensiasit.projectx.services.ContestService;
import com.ensiasit.projectx.utils.Constants;
import com.ensiasit.projectx.utils.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(Constants.API_PREFIX + "/contests")
@RequiredArgsConstructor
public class ContestController {
    private final ContestService contestService;
    private final AdminService adminService;

    @PostMapping
    public ContestDto createContest(Principal principal, @Valid @RequestBody ContestDto contest) {
            return contestService.createContest(principal.getName(), contest);
    }

    @GetMapping
    public List<ContestDto> getAllContests() {
        return contestService.getAll();
    }

    @GetMapping("/current")
    public List<UserContestRoleDto> getUserContests(Principal principal) {
        return contestService.getAllUserContests(principal.getName());
    }

    @GetMapping("/{id}")
    public ContestDto getContest(@PathVariable long id) {
        return contestService.getContest(id);
    }

    @DeleteMapping("/{id}")
    public ContestDto deleteContest(Principal principal, @PathVariable long id) {
        return contestService.deleteContest(principal.getName(), id);
    }

    @PutMapping("/{id}")
    public ContestDto updateContest(Principal principal, @PathVariable Long id, @Valid @RequestBody ContestDto payload) {
        return contestService.updateContest(principal.getName(), id, payload);
    }

    @GetMapping("/{id}/users")
    public List<UserContestRoleDto> getUserContestRoles(@PathVariable long id) {
        return contestService.getUserContestRoles(id);
    }

    @PutMapping("/{contestId}/users/{userId}/{role}")
    public UserContestRoleDto updateUserContestRole(Principal principal, @PathVariable long contestId, @PathVariable long userId, @PathVariable RoleEnum role) {
        return contestService.updateUserContestRole(principal.getName(), contestId, userId, role);
    }
}
