package com.wedevzone.digiparc.service;

import com.wedevzone.digiparc.service.dto.EmplacementDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.wedevzone.digiparc.domain.Emplacement}.
 */
public interface EmplacementService {
    /**
     * Save a emplacement.
     *
     * @param emplacementDTO the entity to save.
     * @return the persisted entity.
     */
    EmplacementDTO save(EmplacementDTO emplacementDTO);

    /**
     * Updates a emplacement.
     *
     * @param emplacementDTO the entity to update.
     * @return the persisted entity.
     */
    EmplacementDTO update(EmplacementDTO emplacementDTO);

    /**
     * Partially updates a emplacement.
     *
     * @param emplacementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmplacementDTO> partialUpdate(EmplacementDTO emplacementDTO);

    /**
     * Get all the emplacements.
     *
     * @return the list of entities.
     */
    List<EmplacementDTO> findAll();

    /**
     * Get the "id" emplacement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmplacementDTO> findOne(Long id);

    /**
     * Delete the "id" emplacement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
