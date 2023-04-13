package com.wedevzone.digiparc.service.mapper;

import com.wedevzone.digiparc.domain.Formation;
import com.wedevzone.digiparc.domain.Subscriber;
import com.wedevzone.digiparc.service.dto.FormationDTO;
import com.wedevzone.digiparc.service.dto.SubscriberDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formation} and its DTO {@link FormationDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormationMapper extends EntityMapper<FormationDTO, Formation> {
    @Mapping(target = "subscribers", source = "subscribers", qualifiedByName = "subscriberCINSet")
    FormationDTO toDto(Formation s);

    @Mapping(target = "removeSubscriber", ignore = true)
    Formation toEntity(FormationDTO formationDTO);

    @Named("subscriberCIN")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cIN", source = "cIN")
    SubscriberDTO toDtoSubscriberCIN(Subscriber subscriber);

    @Named("subscriberCINSet")
    default Set<SubscriberDTO> toDtoSubscriberCINSet(Set<Subscriber> subscriber) {
        return subscriber.stream().map(this::toDtoSubscriberCIN).collect(Collectors.toSet());
    }
}
