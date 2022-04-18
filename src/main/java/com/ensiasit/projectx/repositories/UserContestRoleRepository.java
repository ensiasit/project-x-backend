package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.UserContestRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContestRoleRepository extends JpaRepository<UserContestRole, Long> {
    List<UserContestRole> findAllByUserEmail(String email);

    void deleteAllByContestId(long contestId);
}
