package com.wedevzone.digiparc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wedevzone.digiparc.IntegrationTest;
import com.wedevzone.digiparc.domain.Emplacement;
import com.wedevzone.digiparc.repository.EmplacementRepository;
import com.wedevzone.digiparc.service.dto.EmplacementDTO;
import com.wedevzone.digiparc.service.mapper.EmplacementMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmplacementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmplacementResourceIT {

    private static final Integer DEFAULT_IDENTIFIANT = 1;
    private static final Integer UPDATED_IDENTIFIANT = 2;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CENTRE_DE_FORMATION = "AAAAAAAAAA";
    private static final String UPDATED_CENTRE_DE_FORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_SITE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_WEB_SITE_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/emplacements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmplacementRepository emplacementRepository;

    @Autowired
    private EmplacementMapper emplacementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmplacementMockMvc;

    private Emplacement emplacement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emplacement createEntity(EntityManager em) {
        Emplacement emplacement = new Emplacement()
            .identifiant(DEFAULT_IDENTIFIANT)
            .type(DEFAULT_TYPE)
            .centreDeFormation(DEFAULT_CENTRE_DE_FORMATION)
            .webSiteLink(DEFAULT_WEB_SITE_LINK)
            .adresse(DEFAULT_ADRESSE);
        return emplacement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Emplacement createUpdatedEntity(EntityManager em) {
        Emplacement emplacement = new Emplacement()
            .identifiant(UPDATED_IDENTIFIANT)
            .type(UPDATED_TYPE)
            .centreDeFormation(UPDATED_CENTRE_DE_FORMATION)
            .webSiteLink(UPDATED_WEB_SITE_LINK)
            .adresse(UPDATED_ADRESSE);
        return emplacement;
    }

    @BeforeEach
    public void initTest() {
        emplacement = createEntity(em);
    }

    @Test
    @Transactional
    void createEmplacement() throws Exception {
        int databaseSizeBeforeCreate = emplacementRepository.findAll().size();
        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);
        restEmplacementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeCreate + 1);
        Emplacement testEmplacement = emplacementList.get(emplacementList.size() - 1);
        assertThat(testEmplacement.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
        assertThat(testEmplacement.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEmplacement.getCentreDeFormation()).isEqualTo(DEFAULT_CENTRE_DE_FORMATION);
        assertThat(testEmplacement.getWebSiteLink()).isEqualTo(DEFAULT_WEB_SITE_LINK);
        assertThat(testEmplacement.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void createEmplacementWithExistingId() throws Exception {
        // Create the Emplacement with an existing ID
        emplacement.setId(1L);
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        int databaseSizeBeforeCreate = emplacementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmplacementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentifiantIsRequired() throws Exception {
        int databaseSizeBeforeTest = emplacementRepository.findAll().size();
        // set the field null
        emplacement.setIdentifiant(null);

        // Create the Emplacement, which fails.
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        restEmplacementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = emplacementRepository.findAll().size();
        // set the field null
        emplacement.setType(null);

        // Create the Emplacement, which fails.
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        restEmplacementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isBadRequest());

        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmplacements() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get all the emplacementList
        restEmplacementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emplacement.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifiant").value(hasItem(DEFAULT_IDENTIFIANT)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].centreDeFormation").value(hasItem(DEFAULT_CENTRE_DE_FORMATION)))
            .andExpect(jsonPath("$.[*].webSiteLink").value(hasItem(DEFAULT_WEB_SITE_LINK)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)));
    }

    @Test
    @Transactional
    void getEmplacement() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        // Get the emplacement
        restEmplacementMockMvc
            .perform(get(ENTITY_API_URL_ID, emplacement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emplacement.getId().intValue()))
            .andExpect(jsonPath("$.identifiant").value(DEFAULT_IDENTIFIANT))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.centreDeFormation").value(DEFAULT_CENTRE_DE_FORMATION))
            .andExpect(jsonPath("$.webSiteLink").value(DEFAULT_WEB_SITE_LINK))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE));
    }

    @Test
    @Transactional
    void getNonExistingEmplacement() throws Exception {
        // Get the emplacement
        restEmplacementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmplacement() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();

        // Update the emplacement
        Emplacement updatedEmplacement = emplacementRepository.findById(emplacement.getId()).get();
        // Disconnect from session so that the updates on updatedEmplacement are not directly saved in db
        em.detach(updatedEmplacement);
        updatedEmplacement
            .identifiant(UPDATED_IDENTIFIANT)
            .type(UPDATED_TYPE)
            .centreDeFormation(UPDATED_CENTRE_DE_FORMATION)
            .webSiteLink(UPDATED_WEB_SITE_LINK)
            .adresse(UPDATED_ADRESSE);
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(updatedEmplacement);

        restEmplacementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emplacementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
        Emplacement testEmplacement = emplacementList.get(emplacementList.size() - 1);
        assertThat(testEmplacement.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testEmplacement.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEmplacement.getCentreDeFormation()).isEqualTo(UPDATED_CENTRE_DE_FORMATION);
        assertThat(testEmplacement.getWebSiteLink()).isEqualTo(UPDATED_WEB_SITE_LINK);
        assertThat(testEmplacement.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void putNonExistingEmplacement() throws Exception {
        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();
        emplacement.setId(count.incrementAndGet());

        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmplacementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emplacementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmplacement() throws Exception {
        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();
        emplacement.setId(count.incrementAndGet());

        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmplacementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmplacement() throws Exception {
        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();
        emplacement.setId(count.incrementAndGet());

        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmplacementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(emplacementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmplacementWithPatch() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();

        // Update the emplacement using partial update
        Emplacement partialUpdatedEmplacement = new Emplacement();
        partialUpdatedEmplacement.setId(emplacement.getId());

        partialUpdatedEmplacement
            .centreDeFormation(UPDATED_CENTRE_DE_FORMATION)
            .webSiteLink(UPDATED_WEB_SITE_LINK)
            .adresse(UPDATED_ADRESSE);

        restEmplacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmplacement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmplacement))
            )
            .andExpect(status().isOk());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
        Emplacement testEmplacement = emplacementList.get(emplacementList.size() - 1);
        assertThat(testEmplacement.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
        assertThat(testEmplacement.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEmplacement.getCentreDeFormation()).isEqualTo(UPDATED_CENTRE_DE_FORMATION);
        assertThat(testEmplacement.getWebSiteLink()).isEqualTo(UPDATED_WEB_SITE_LINK);
        assertThat(testEmplacement.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void fullUpdateEmplacementWithPatch() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();

        // Update the emplacement using partial update
        Emplacement partialUpdatedEmplacement = new Emplacement();
        partialUpdatedEmplacement.setId(emplacement.getId());

        partialUpdatedEmplacement
            .identifiant(UPDATED_IDENTIFIANT)
            .type(UPDATED_TYPE)
            .centreDeFormation(UPDATED_CENTRE_DE_FORMATION)
            .webSiteLink(UPDATED_WEB_SITE_LINK)
            .adresse(UPDATED_ADRESSE);

        restEmplacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmplacement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmplacement))
            )
            .andExpect(status().isOk());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
        Emplacement testEmplacement = emplacementList.get(emplacementList.size() - 1);
        assertThat(testEmplacement.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testEmplacement.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEmplacement.getCentreDeFormation()).isEqualTo(UPDATED_CENTRE_DE_FORMATION);
        assertThat(testEmplacement.getWebSiteLink()).isEqualTo(UPDATED_WEB_SITE_LINK);
        assertThat(testEmplacement.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void patchNonExistingEmplacement() throws Exception {
        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();
        emplacement.setId(count.incrementAndGet());

        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmplacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emplacementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmplacement() throws Exception {
        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();
        emplacement.setId(count.incrementAndGet());

        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmplacementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmplacement() throws Exception {
        int databaseSizeBeforeUpdate = emplacementRepository.findAll().size();
        emplacement.setId(count.incrementAndGet());

        // Create the Emplacement
        EmplacementDTO emplacementDTO = emplacementMapper.toDto(emplacement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmplacementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(emplacementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Emplacement in the database
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmplacement() throws Exception {
        // Initialize the database
        emplacementRepository.saveAndFlush(emplacement);

        int databaseSizeBeforeDelete = emplacementRepository.findAll().size();

        // Delete the emplacement
        restEmplacementMockMvc
            .perform(delete(ENTITY_API_URL_ID, emplacement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Emplacement> emplacementList = emplacementRepository.findAll();
        assertThat(emplacementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
