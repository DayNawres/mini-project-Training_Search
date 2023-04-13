package com.wedevzone.digiparc.web.rest;

import com.wedevzone.digiparc.repository.EmplacementRepository;
import com.wedevzone.digiparc.service.EmplacementService;
import com.wedevzone.digiparc.service.dto.EmplacementDTO;
import com.wedevzone.digiparc.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.wedevzone.digiparc.domain.Emplacement}.
 */
@RestController
@RequestMapping("/api")
public class EmplacementResource {

    private final Logger log = LoggerFactory.getLogger(EmplacementResource.class);

    private static final String ENTITY_NAME = "emplacement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmplacementService emplacementService;

    private final EmplacementRepository emplacementRepository;

    public EmplacementResource(EmplacementService emplacementService, EmplacementRepository emplacementRepository) {
        this.emplacementService = emplacementService;
        this.emplacementRepository = emplacementRepository;
    }

    /**
     * {@code POST  /emplacements} : Create a new emplacement.
     *
     * @param emplacementDTO the emplacementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new emplacementDTO, or with status {@code 400 (Bad Request)} if the emplacement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/emplacements")
    public ResponseEntity<EmplacementDTO> createEmplacement(@Valid @RequestBody EmplacementDTO emplacementDTO) throws URISyntaxException {
        log.debug("REST request to save Emplacement : {}", emplacementDTO);
        if (emplacementDTO.getId() != null) {
            throw new BadRequestAlertException("A new emplacement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmplacementDTO result = emplacementService.save(emplacementDTO);
        return ResponseEntity
            .created(new URI("/api/emplacements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /emplacements/:id} : Updates an existing emplacement.
     *
     * @param id the id of the emplacementDTO to save.
     * @param emplacementDTO the emplacementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emplacementDTO,
     * or with status {@code 400 (Bad Request)} if the emplacementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the emplacementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/emplacements/{id}")
    public ResponseEntity<EmplacementDTO> updateEmplacement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmplacementDTO emplacementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Emplacement : {}, {}", id, emplacementDTO);
        if (emplacementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emplacementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emplacementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmplacementDTO result = emplacementService.update(emplacementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emplacementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /emplacements/:id} : Partial updates given fields of an existing emplacement, field will ignore if it is null
     *
     * @param id the id of the emplacementDTO to save.
     * @param emplacementDTO the emplacementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated emplacementDTO,
     * or with status {@code 400 (Bad Request)} if the emplacementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the emplacementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the emplacementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/emplacements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmplacementDTO> partialUpdateEmplacement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmplacementDTO emplacementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Emplacement partially : {}, {}", id, emplacementDTO);
        if (emplacementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, emplacementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!emplacementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmplacementDTO> result = emplacementService.partialUpdate(emplacementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, emplacementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /emplacements} : get all the emplacements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of emplacements in body.
     */
    @GetMapping("/emplacements")
    public List<EmplacementDTO> getAllEmplacements() {
        log.debug("REST request to get all Emplacements");
        return emplacementService.findAll();
    }

    /**
     * {@code GET  /emplacements/:id} : get the "id" emplacement.
     *
     * @param id the id of the emplacementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the emplacementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/emplacements/{id}")
    public ResponseEntity<EmplacementDTO> getEmplacement(@PathVariable Long id) {
        log.debug("REST request to get Emplacement : {}", id);
        Optional<EmplacementDTO> emplacementDTO = emplacementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(emplacementDTO);
    }

    /**
     * {@code DELETE  /emplacements/:id} : delete the "id" emplacement.
     *
     * @param id the id of the emplacementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/emplacements/{id}")
    public ResponseEntity<Void> deleteEmplacement(@PathVariable Long id) {
        log.debug("REST request to delete Emplacement : {}", id);
        emplacementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
