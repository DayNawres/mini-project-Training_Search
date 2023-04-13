package com.wedevzone.digiparc.service;

import com.wedevzone.digiparc.service.dto.FormationDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.wedevzone.digiparc.domain.Formation}.
 */
public interface FormationService {
    /**
     * Save a formation.
     *
     * @param formationDTO the entity to save.
     * @return the persisted entity.
     */
    FormationDTO save(FormationDTO formationDTO);

    /**
     * Updates a formation.
     *
     * @param formationDTO the entity to update.
     * @return the persisted entity.
     */
    FormationDTO update(FormationDTO formationDTO);

    /**
     * Partially updates a formation.
     *
     * @param formationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormationDTO> partialUpdate(FormationDTO formationDTO);

    /**
     * Get all the formations.
     *
     * @return the list of entities.
     */
    List<FormationDTO> findAll();

    /**
     * Get all the formations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" formation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormationDTO> findOne(Long id);

    /**
     * Delete the "id" formation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
