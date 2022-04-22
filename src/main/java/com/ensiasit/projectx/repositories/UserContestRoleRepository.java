package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.UserContestRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserContestRoleRepository extends JpaRepository<UserContestRole, Long> {
    List<UserContestRole> findAllByUserEmail(String email);

    void deleteAllByContestId(long contestId);

    List<UserContestRole> findAllByContestId(long id);

    Optional<UserContestRole> findByContestIdAndUserId(long contestId, long userId);

    Optional<UserContestRole> findByContestIdAndUserEmail(long contestId, String userEmail);
}
