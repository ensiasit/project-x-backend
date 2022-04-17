package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Contest deleteContestById(long id);
}
