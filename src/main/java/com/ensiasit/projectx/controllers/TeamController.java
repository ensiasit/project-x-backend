package com.ensiasit.projectx.controllers;

import com.ensiasit.projectx.dto.TeamDto;
import com.ensiasit.projectx.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.ensiasit.projectx.utils.Constants.API_PREFIX;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(API_PREFIX + "/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public TeamDto createTeam(@Valid @RequestBody TeamDto team) {
        return teamService.createTeam(team);
    }

    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    public TeamDto getTeam(@PathVariable long id) {
        return teamService.getTeam(id);
    }

    @DeleteMapping("/{id}")
    public TeamDto deleteTeam(@PathVariable long id) {
        return teamService.deleteTeam(id);
    }

    @PutMapping("/{id}")
    private TeamDto updateTeam(@PathVariable Long id, @Valid @RequestBody TeamDto payload) {
        return teamService.updateTeam(id, payload);
    }
}
