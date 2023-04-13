package com.wedevzone.digiparc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wedevzone.digiparc.IntegrationTest;
import com.wedevzone.digiparc.domain.Emplacement;
import com.wedevzone.digiparc.domain.Formation;
import com.wedevzone.digiparc.domain.enumeration.Type;
import com.wedevzone.digiparc.repository.FormationRepository;
import com.wedevzone.digiparc.service.FormationService;
import com.wedevzone.digiparc.service.dto.FormationDTO;
import com.wedevzone.digiparc.service.mapper.FormationMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FormationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FormationResourceIT {

    private static final String DEFAULT_IDENTIFIANT = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIANT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAINE = "AAAAAAAAAA";
    private static final String UPDATED_DOMAINE = "BBBBBBBBBB";

    private static final Type DEFAULT_TYPE = Type.EnLigne;
    private static final Type UPDATED_TYPE = Type.Presentielle;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LES_HEURES_DE_LA_FORMATION = "AAAAAAAAAA";
    private static final String UPDATED_LES_HEURES_DE_LA_FORMATION = "BBBBBBBBBB";

    private static final String DEFAULT_LE_PRIX = "AAAAAAAAAA";
    private static final String UPDATED_LE_PRIX = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_INSTRUCTEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_INSTRUCTEUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/formations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormationRepository formationRepository;

    @Mock
    private FormationRepository formationRepositoryMock;

    @Autowired
    private FormationMapper formationMapper;

    @Mock
    private FormationService formationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormationMockMvc;

    private Formation formation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createEntity(EntityManager em) {
        Formation formation = new Formation()
            .identifiant(DEFAULT_IDENTIFIANT)
            .description(DEFAULT_DESCRIPTION)
            .domaine(DEFAULT_DOMAINE)
            .type(DEFAULT_TYPE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .lesHeuresDeLaFormation(DEFAULT_LES_HEURES_DE_LA_FORMATION)
            .lePrix(DEFAULT_LE_PRIX)
            .nomInstructeur(DEFAULT_NOM_INSTRUCTEUR);
        // Add required entity
        Emplacement emplacement;
        if (TestUtil.findAll(em, Emplacement.class).isEmpty()) {
            emplacement = EmplacementResourceIT.createEntity(em);
            em.persist(emplacement);
            em.flush();
        } else {
            emplacement = TestUtil.findAll(em, Emplacement.class).get(0);
        }
        formation.getEmplacaments().add(emplacement);
        return formation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formation createUpdatedEntity(EntityManager em) {
        Formation formation = new Formation()
            .identifiant(UPDATED_IDENTIFIANT)
            .description(UPDATED_DESCRIPTION)
            .domaine(UPDATED_DOMAINE)
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .lesHeuresDeLaFormation(UPDATED_LES_HEURES_DE_LA_FORMATION)
            .lePrix(UPDATED_LE_PRIX)
            .nomInstructeur(UPDATED_NOM_INSTRUCTEUR);
        // Add required entity
        Emplacement emplacement;
        if (TestUtil.findAll(em, Emplacement.class).isEmpty()) {
            emplacement = EmplacementResourceIT.createUpdatedEntity(em);
            em.persist(emplacement);
            em.flush();
        } else {
            emplacement = TestUtil.findAll(em, Emplacement.class).get(0);
        }
        formation.getEmplacaments().add(emplacement);
        return formation;
    }

    @BeforeEach
    public void initTest() {
        formation = createEntity(em);
    }

    @Test
    @Transactional
    void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();
        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);
        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
        assertThat(testFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormation.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testFormation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFormation.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFormation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFormation.getLesHeuresDeLaFormation()).isEqualTo(DEFAULT_LES_HEURES_DE_LA_FORMATION);
        assertThat(testFormation.getLePrix()).isEqualTo(DEFAULT_LE_PRIX);
        assertThat(testFormation.getNomInstructeur()).isEqualTo(DEFAULT_NOM_INSTRUCTEUR);
    }

    @Test
    @Transactional
    void createFormationWithExistingId() throws Exception {
        // Create the Formation with an existing ID
        formation.setId(1L);
        FormationDTO formationDTO = formationMapper.toDto(formation);

        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentifiantIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setIdentifiant(null);

        // Create the Formation, which fails.
        FormationDTO formationDTO = formationMapper.toDto(formation);

        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setDescription(null);

        // Create the Formation, which fails.
        FormationDTO formationDTO = formationMapper.toDto(formation);

        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setDateDebut(null);

        // Create the Formation, which fails.
        FormationDTO formationDTO = formationMapper.toDto(formation);

        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setDateFin(null);

        // Create the Formation, which fails.
        FormationDTO formationDTO = formationMapper.toDto(formation);

        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLesHeuresDeLaFormationIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setLesHeuresDeLaFormation(null);

        // Create the Formation, which fails.
        FormationDTO formationDTO = formationMapper.toDto(formation);

        restFormationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isBadRequest());

        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formationList
        restFormationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifiant").value(hasItem(DEFAULT_IDENTIFIANT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].domaine").value(hasItem(DEFAULT_DOMAINE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].lesHeuresDeLaFormation").value(hasItem(DEFAULT_LES_HEURES_DE_LA_FORMATION)))
            .andExpect(jsonPath("$.[*].lePrix").value(hasItem(DEFAULT_LE_PRIX)))
            .andExpect(jsonPath("$.[*].nomInstructeur").value(hasItem(DEFAULT_NOM_INSTRUCTEUR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(formationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(formationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(formationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(formationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc
            .perform(get(ENTITY_API_URL_ID, formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.identifiant").value(DEFAULT_IDENTIFIANT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.domaine").value(DEFAULT_DOMAINE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.lesHeuresDeLaFormation").value(DEFAULT_LES_HEURES_DE_LA_FORMATION))
            .andExpect(jsonPath("$.lePrix").value(DEFAULT_LE_PRIX))
            .andExpect(jsonPath("$.nomInstructeur").value(DEFAULT_NOM_INSTRUCTEUR));
    }

    @Test
    @Transactional
    void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = formationRepository.findById(formation.getId()).get();
        // Disconnect from session so that the updates on updatedFormation are not directly saved in db
        em.detach(updatedFormation);
        updatedFormation
            .identifiant(UPDATED_IDENTIFIANT)
            .description(UPDATED_DESCRIPTION)
            .domaine(UPDATED_DOMAINE)
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .lesHeuresDeLaFormation(UPDATED_LES_HEURES_DE_LA_FORMATION)
            .lePrix(UPDATED_LE_PRIX)
            .nomInstructeur(UPDATED_NOM_INSTRUCTEUR);
        FormationDTO formationDTO = formationMapper.toDto(updatedFormation);

        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormation.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testFormation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFormation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFormation.getLesHeuresDeLaFormation()).isEqualTo(UPDATED_LES_HEURES_DE_LA_FORMATION);
        assertThat(testFormation.getLePrix()).isEqualTo(UPDATED_LE_PRIX);
        assertThat(testFormation.getNomInstructeur()).isEqualTo(UPDATED_NOM_INSTRUCTEUR);
    }

    @Test
    @Transactional
    void putNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation.dateDebut(UPDATED_DATE_DEBUT).nomInstructeur(UPDATED_NOM_INSTRUCTEUR);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
        assertThat(testFormation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormation.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testFormation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFormation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormation.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testFormation.getLesHeuresDeLaFormation()).isEqualTo(DEFAULT_LES_HEURES_DE_LA_FORMATION);
        assertThat(testFormation.getLePrix()).isEqualTo(DEFAULT_LE_PRIX);
        assertThat(testFormation.getNomInstructeur()).isEqualTo(UPDATED_NOM_INSTRUCTEUR);
    }

    @Test
    @Transactional
    void fullUpdateFormationWithPatch() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation using partial update
        Formation partialUpdatedFormation = new Formation();
        partialUpdatedFormation.setId(formation.getId());

        partialUpdatedFormation
            .identifiant(UPDATED_IDENTIFIANT)
            .description(UPDATED_DESCRIPTION)
            .domaine(UPDATED_DOMAINE)
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .lesHeuresDeLaFormation(UPDATED_LES_HEURES_DE_LA_FORMATION)
            .lePrix(UPDATED_LE_PRIX)
            .nomInstructeur(UPDATED_NOM_INSTRUCTEUR);

        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormation))
            )
            .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formationList.get(formationList.size() - 1);
        assertThat(testFormation.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testFormation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormation.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testFormation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFormation.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormation.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testFormation.getLesHeuresDeLaFormation()).isEqualTo(UPDATED_LES_HEURES_DE_LA_FORMATION);
        assertThat(testFormation.getLePrix()).isEqualTo(UPDATED_LE_PRIX);
        assertThat(testFormation.getNomInstructeur()).isEqualTo(UPDATED_NOM_INSTRUCTEUR);
    }

    @Test
    @Transactional
    void patchNonExistingFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormation() throws Exception {
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();
        formation.setId(count.incrementAndGet());

        // Create the Formation
        FormationDTO formationDTO = formationMapper.toDto(formation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formation in the database
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Delete the formation
        restFormationMockMvc
            .perform(delete(ENTITY_API_URL_ID, formation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formation> formationList = formationRepository.findAll();
        assertThat(formationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
