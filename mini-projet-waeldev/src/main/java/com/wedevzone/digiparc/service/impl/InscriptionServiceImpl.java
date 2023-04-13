package com.wedevzone.digiparc.service.impl;

import com.wedevzone.digiparc.domain.Inscription;
import com.wedevzone.digiparc.repository.InscriptionRepository;
import com.wedevzone.digiparc.service.InscriptionService;
import com.wedevzone.digiparc.service.dto.InscriptionDTO;
import com.wedevzone.digiparc.service.mapper.InscriptionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Inscription}.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionServiceImpl.class);

    private final InscriptionRepository inscriptionRepository;

    private final InscriptionMapper inscriptionMapper;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository, InscriptionMapper inscriptionMapper) {
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionMapper = inscriptionMapper;
    }

    @Override
    public InscriptionDTO save(InscriptionDTO inscriptionDTO) {
        log.debug("Request to save Inscription : {}", inscriptionDTO);
        Inscription inscription = inscriptionMapper.toEntity(inscriptionDTO);
        inscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDto(inscription);
    }

    @Override
    public InscriptionDTO update(InscriptionDTO inscriptionDTO) {
        log.debug("Request to save Inscription : {}", inscriptionDTO);
        Inscription inscription = inscriptionMapper.toEntity(inscriptionDTO);
        inscription = inscriptionRepository.save(inscription);
        return inscriptionMapper.toDto(inscription);
    }

    @Override
    public Optional<InscriptionDTO> partialUpdate(InscriptionDTO inscriptionDTO) {
        log.debug("Request to partially update Inscription : {}", inscriptionDTO);

        return inscriptionRepository
            .findById(inscriptionDTO.getId())
            .map(existingInscription -> {
                inscriptionMapper.partialUpdate(existingInscription, inscriptionDTO);

                return existingInscription;
            })
            .map(inscriptionRepository::save)
            .map(inscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscriptionDTO> findAll() {
        log.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll().stream().map(inscriptionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<InscriptionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return inscriptionRepository.findAllWithEagerRelationships(pageable).map(inscriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InscriptionDTO> findOne(Long id) {
        log.debug("Request to get Inscription : {}", id);
        return inscriptionRepository.findOneWithEagerRelationships(id).map(inscriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
    }
}
