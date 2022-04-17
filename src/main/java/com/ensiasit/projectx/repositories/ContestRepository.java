package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {
    Contest deleteContestById(long id);
}
