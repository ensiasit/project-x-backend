package com.ensiasit.projectx.services;

import com.ensiasit.projectx.models.Affiliation;
import com.ensiasit.projectx.repositories.AffiliationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AffiliationServiceImpl implements AffiliationService{

    private AffiliationRepository affiliationRepository;
    @Override
    public List<Affiliation> getAll() {
        return affiliationRepository.findAll();
    }

    @Override
    public Optional<Affiliation> get(Long id) {
        return affiliationRepository.findById(id);
    }

    @Override
    public Affiliation add(Affiliation affiliation) {
        return affiliationRepository.save(affiliation);
    }

    @Override
    public void delete(Long id) {
        affiliationRepository.deleteById(id);
    }

    @Override
    public Optional<Affiliation> update(Long id, Affiliation payload) {
        Optional<Affiliation> optionalAffiliation = get(id);
        if(optionalAffiliation.isPresent()){
            Affiliation affiliation = optionalAffiliation.get();
            affiliation.setCountry(payload.getCountry());
            affiliation.setName(payload.getName());
            affiliation.setLogo(payload.getLogo());
            affiliationRepository.save(affiliation); //fixme does this save updates affiliation or creates a new one
            return Optional.of(affiliation);
        }
        else{
            return optionalAffiliation;
        }
    }
}
