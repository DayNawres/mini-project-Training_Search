package com.wedevzone.digiparc.service;

import com.wedevzone.digiparc.service.dto.SubscriberDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.wedevzone.digiparc.domain.Subscriber}.
 */
public interface SubscriberService {
    /**
     * Save a subscriber.
     *
     * @param subscriberDTO the entity to save.
     * @return the persisted entity.
     */
    SubscriberDTO save(SubscriberDTO subscriberDTO);

    /**
     * Updates a subscriber.
     *
     * @param subscriberDTO the entity to update.
     * @return the persisted entity.
     */
    SubscriberDTO update(SubscriberDTO subscriberDTO);

    /**
     * Partially updates a subscriber.
     *
     * @param subscriberDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubscriberDTO> partialUpdate(SubscriberDTO subscriberDTO);

    /**
     * Get all the subscribers.
     *
     * @return the list of entities.
     */
    List<SubscriberDTO> findAll();

    /**
     * Get the "id" subscriber.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubscriberDTO> findOne(Long id);

    /**
     * Delete the "id" subscriber.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
