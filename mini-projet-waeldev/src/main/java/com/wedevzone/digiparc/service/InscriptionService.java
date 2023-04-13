package com.wedevzone.digiparc.service;

import com.wedevzone.digiparc.service.dto.InscriptionDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.wedevzone.digiparc.domain.Inscription}.
 */
public interface InscriptionService {
    /**
     * Save a inscription.
     *
     * @param inscriptionDTO the entity to save.
     * @return the persisted entity.
     */
    InscriptionDTO save(InscriptionDTO inscriptionDTO);

    /**
     * Updates a inscription.
     *
     * @param inscriptionDTO the entity to update.
     * @return the persisted entity.
     */
    InscriptionDTO update(InscriptionDTO inscriptionDTO);

    /**
     * Partially updates a inscription.
     *
     * @param inscriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InscriptionDTO> partialUpdate(InscriptionDTO inscriptionDTO);

    /**
     * Get all the inscriptions.
     *
     * @return the list of entities.
     */
    List<InscriptionDTO> findAll();

    /**
     * Get all the inscriptions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InscriptionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" inscription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InscriptionDTO> findOne(Long id);

    /**
     * Delete the "id" inscription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
