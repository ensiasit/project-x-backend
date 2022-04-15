package com.ensiasit.projectx.services;

import com.ensiasit.projectx.models.Contest;
import com.ensiasit.projectx.repositories.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Override
    public List<Contest> getAll() {
        return contestRepository.findAll();
    }

    @Override
    public Contest createContest(Contest payload) {
        return contestRepository.save(payload);
    }

    @Override
    public Optional<Contest> getContest(Long id) {
        return contestRepository.findById(id);
    }

    @Override
    public void deleteContest(Long id) {
        contestRepository.deleteById(id);
    }
}
