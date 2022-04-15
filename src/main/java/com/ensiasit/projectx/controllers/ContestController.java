package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.dto.ContestDto;
import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.services.ContestService;
import com.ensiasit.projectx.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(Constants.API_PREFIX + "/contests")
public class ContestController {
    private final ContestService contestService;

    @PostMapping
    public ContestDto createContest(@Valid @RequestBody ContestDto contest) {
        return contestService.createContest(contest);
    }

    @GetMapping
    public List<Contest> getAllContests() {
        return contestService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestDto> getContest(@PathVariable long id) {
        Optional<ContestDto> optionalContest = contestService.getContest(id);

        if (optionalContest.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalContest.get());
    }

    @DeleteMapping("/{id}")
    public void deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
    }
}
