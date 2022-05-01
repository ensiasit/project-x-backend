package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.MemberRequest;
import com.ensiasit.projectx.dto.MemberResponse;
import com.ensiasit.projectx.dto.TeamRequest;
import com.ensiasit.projectx.dto.TeamResponse;
import com.ensiasit.projectx.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.ensiasit.projectx.utils.Constants.API_PREFIX;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(API_PREFIX + "/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public TeamResponse createTeam(Principal principal, @Valid @RequestBody TeamRequest team) {
        return teamService.createTeam(principal.getName(), team);
    }

    @GetMapping
    public List<TeamResponse> getAllTeams() {
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    public TeamResponse getTeam(@PathVariable long id) {
        return teamService.getTeam(id);
    }

    @DeleteMapping("/{id}")
    public TeamResponse deleteTeam(Principal principal, @PathVariable long id) {
        return teamService.deleteTeam(principal.getName(), id);
    }

    @PutMapping("/{id}")
    private TeamResponse updateTeam(Principal principal, @PathVariable long id, @Valid @RequestBody TeamRequest payload) {
        return teamService.updateTeam(principal.getName(), id, payload);
    }

    @PostMapping("/{id}/users")
    public TeamResponse addMember(Principal principal, @PathVariable long id, @Valid @RequestBody MemberRequest member) {
        return teamService.addMember(principal.getName(), id, member);
    }
}
