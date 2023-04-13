package com.wedevzone.digiparc.service.mapper;

import com.wedevzone.digiparc.domain.Emplacement;
import com.wedevzone.digiparc.domain.Formation;
import com.wedevzone.digiparc.service.dto.EmplacementDTO;
import com.wedevzone.digiparc.service.dto.FormationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Emplacement} and its DTO {@link EmplacementDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmplacementMapper extends EntityMapper<EmplacementDTO, Emplacement> {
    @Mapping(target = "formation", source = "formation", qualifiedByName = "formationId")
    EmplacementDTO toDto(Emplacement s);

    @Named("formationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FormationDTO toDtoFormationId(Formation formation);
}
