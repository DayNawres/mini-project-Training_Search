package com.wedevzone.digiparc.service.mapper;

import com.wedevzone.digiparc.domain.Inscription;
import com.wedevzone.digiparc.domain.Subscriber;
import com.wedevzone.digiparc.service.dto.InscriptionDTO;
import com.wedevzone.digiparc.service.dto.SubscriberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Subscriber} and its DTO {@link SubscriberDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubscriberMapper extends EntityMapper<SubscriberDTO, Subscriber> {
    @Mapping(target = "inscription", source = "inscription", qualifiedByName = "inscriptionId")
    SubscriberDTO toDto(Subscriber s);

    @Named("inscriptionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InscriptionDTO toDtoInscriptionId(Inscription inscription);
}
