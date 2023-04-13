package com.wedevzone.digiparc.service.impl;

import com.wedevzone.digiparc.domain.Emplacement;
import com.wedevzone.digiparc.repository.EmplacementRepository;
import com.wedevzone.digiparc.service.EmplacementService;
import com.wedevzone.digiparc.service.dto.EmplacementDTO;
import com.wedevzone.digiparc.service.mapper.EmplacementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Emplacement}.
 */
@Service
@Transactional
public class EmplacementServiceImpl implements EmplacementService {

    private final Logger log = LoggerFactory.getLogger(EmplacementServiceImpl.class);

    private final EmplacementRepository emplacementRepository;

    private final EmplacementMapper emplacementMapper;

    public EmplacementServiceImpl(EmplacementRepository emplacementRepository, EmplacementMapper emplacementMapper) {
        this.emplacementRepository = emplacementRepository;
        this.emplacementMapper = emplacementMapper;
    }

    @Override
    public EmplacementDTO save(EmplacementDTO emplacementDTO) {
        log.debug("Request to save Emplacement : {}", emplacementDTO);
        Emplacement emplacement = emplacementMapper.toEntity(emplacementDTO);
        emplacement = emplacementRepository.save(emplacement);
        return emplacementMapper.toDto(emplacement);
    }

    @Override
    public EmplacementDTO update(EmplacementDTO emplacementDTO) {
        log.debug("Request to save Emplacement : {}", emplacementDTO);
        Emplacement emplacement = emplacementMapper.toEntity(emplacementDTO);
        emplacement = emplacementRepository.save(emplacement);
        return emplacementMapper.toDto(emplacement);
    }

    @Override
    public Optional<EmplacementDTO> partialUpdate(EmplacementDTO emplacementDTO) {
        log.debug("Request to partially update Emplacement : {}", emplacementDTO);

        return emplacementRepository
            .findById(emplacementDTO.getId())
            .map(existingEmplacement -> {
                emplacementMapper.partialUpdate(existingEmplacement, emplacementDTO);

                return existingEmplacement;
            })
            .map(emplacementRepository::save)
            .map(emplacementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmplacementDTO> findAll() {
        log.debug("Request to get all Emplacements");
        return emplacementRepository.findAll().stream().map(emplacementMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmplacementDTO> findOne(Long id) {
        log.debug("Request to get Emplacement : {}", id);
        return emplacementRepository.findById(id).map(emplacementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Emplacement : {}", id);
        emplacementRepository.deleteById(id);
    }
}
