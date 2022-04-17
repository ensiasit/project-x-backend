package com.ensiasit.projectx.services;

import com.ensiasit.projectx.models.Affiliation;

import java.util.List;
import java.util.Optional;

public interface AffiliationService {
    List<Affiliation> getAll();

    Optional<Affiliation> get(Long id);

    Affiliation add(Affiliation affiliation);

    void delete(Long id);

    Optional<Affiliation> update(Long id, Affiliation payload);
}
