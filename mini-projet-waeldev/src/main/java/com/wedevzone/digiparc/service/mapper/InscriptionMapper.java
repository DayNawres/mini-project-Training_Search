package com.wedevzone.digiparc.service.mapper;

import com.wedevzone.digiparc.domain.Formation;
import com.wedevzone.digiparc.domain.Inscription;
import com.wedevzone.digiparc.service.dto.FormationDTO;
import com.wedevzone.digiparc.service.dto.InscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inscription} and its DTO {@link InscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InscriptionMapper extends EntityMapper<InscriptionDTO, Inscription> {
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationIdentifiant")
    InscriptionDTO toDto(Inscription s);

    @Named("formationIdentifiant")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "identifiant", source = "identifiant")
    FormationDTO toDtoFormationIdentifiant(Formation formation);
}
