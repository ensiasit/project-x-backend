package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);
}
