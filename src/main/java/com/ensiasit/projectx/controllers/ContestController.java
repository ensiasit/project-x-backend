package com.ensiasit.projectx.controllers;


import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/contest")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @PostMapping("/create")
    public Contest createContest(HttpServletRequest request) {
        Contest contest = new Contest();
        contest.setName(request.getParameter("name"));
        contest.setStartTime(LocalDateTime.parse(request.getParameter("startTime")));
        contest.setEndTime(LocalDateTime.parse(request.getParameter("endTime")));
        contest.setFreezeTime(LocalDateTime.parse(request.getParameter("freezeTime")));
        contest.setUnfreezeTime(LocalDateTime.parse(request.getParameter("startTime")));
        return contestService.createContest(contest);
    }

    @GetMapping("/get/all")
    public List<Contest> getAllContests() {
        return contestService.getAll();
    }

    @GetMapping("/get/{id}")
    public Optional<Contest> getContest(@PathVariable Long id) {
        return contestService.getContest(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
    }
}
