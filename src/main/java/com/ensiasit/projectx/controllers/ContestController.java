package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.dto.UserContestRoleDto;
import com.ensiasit.projectx.services.ContestService;
import com.ensiasit.projectx.utils.Constants;
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

    @PostMapping
    public ContestDto createContest(@Valid @RequestBody ContestDto contest) {
        return contestService.createContest(contest);
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
    public ContestDto deleteContest(@PathVariable long id) {
        return contestService.deleteContest(id);
    }

    @PutMapping("/{id}")
    private ContestDto updateContest(@PathVariable Long id, @Valid @RequestBody ContestDto payload) {
        return contestService.updateContest(id, payload);
    }
}
