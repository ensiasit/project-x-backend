package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByUserEmail(String userEmail);
}
