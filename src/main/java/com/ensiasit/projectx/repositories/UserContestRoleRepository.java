package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.UserContestRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContestRoleRepository extends JpaRepository<UserContestRole, Long> {
}
