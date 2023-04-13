package com.wedevzone.digiparc.service.impl;

import com.wedevzone.digiparc.domain.Subscriber;
import com.wedevzone.digiparc.repository.SubscriberRepository;
import com.wedevzone.digiparc.service.SubscriberService;
import com.wedevzone.digiparc.service.dto.SubscriberDTO;
import com.wedevzone.digiparc.service.mapper.SubscriberMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Subscriber}.
 */
@Service
@Transactional
public class SubscriberServiceImpl implements SubscriberService {

    private final Logger log = LoggerFactory.getLogger(SubscriberServiceImpl.class);

    private final SubscriberRepository subscriberRepository;

    private final SubscriberMapper subscriberMapper;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, SubscriberMapper subscriberMapper) {
        this.subscriberRepository = subscriberRepository;
        this.subscriberMapper = subscriberMapper;
    }

    @Override
    public SubscriberDTO save(SubscriberDTO subscriberDTO) {
        log.debug("Request to save Subscriber : {}", subscriberDTO);
        Subscriber subscriber = subscriberMapper.toEntity(subscriberDTO);
        subscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toDto(subscriber);
    }

    @Override
    public SubscriberDTO update(SubscriberDTO subscriberDTO) {
        log.debug("Request to save Subscriber : {}", subscriberDTO);
        Subscriber subscriber = subscriberMapper.toEntity(subscriberDTO);
        subscriber = subscriberRepository.save(subscriber);
        return subscriberMapper.toDto(subscriber);
    }

    @Override
    public Optional<SubscriberDTO> partialUpdate(SubscriberDTO subscriberDTO) {
        log.debug("Request to partially update Subscriber : {}", subscriberDTO);

        return subscriberRepository
            .findById(subscriberDTO.getId())
            .map(existingSubscriber -> {
                subscriberMapper.partialUpdate(existingSubscriber, subscriberDTO);

                return existingSubscriber;
            })
            .map(subscriberRepository::save)
            .map(subscriberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriberDTO> findAll() {
        log.debug("Request to get all Subscribers");
        return subscriberRepository.findAll().stream().map(subscriberMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SubscriberDTO> findOne(Long id) {
        log.debug("Request to get Subscriber : {}", id);
        return subscriberRepository.findById(id).map(subscriberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Subscriber : {}", id);
        subscriberRepository.deleteById(id);
    }
}
