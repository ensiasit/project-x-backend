package com.ensiasit.projectx.repositories;

import com.ensiasit.projectx.models.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {
    Affiliation deleteAffiliationById(long id);
}
