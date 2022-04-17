package com.ensiasit.projectx.mappers;

import com.ensiasit.projectx.dtos.AffiliationDTO;
import com.ensiasit.projectx.models.Affiliation;
import org.springframework.core.convert.converter.Converter;

public interface AffiliationMapper extends Converter<Affiliation, AffiliationDTO> {



}
