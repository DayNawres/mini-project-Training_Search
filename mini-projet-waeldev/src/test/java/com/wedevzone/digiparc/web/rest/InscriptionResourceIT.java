package com.wedevzone.digiparc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wedevzone.digiparc.IntegrationTest;
import com.wedevzone.digiparc.domain.Formation;
import com.wedevzone.digiparc.domain.Inscription;
import com.wedevzone.digiparc.domain.Subscriber;
import com.wedevzone.digiparc.repository.InscriptionRepository;
import com.wedevzone.digiparc.service.InscriptionService;
import com.wedevzone.digiparc.service.dto.InscriptionDTO;
import com.wedevzone.digiparc.service.mapper.InscriptionMapper;
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
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InscriptionResourceIT {

    private static final String DEFAULT_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_OBJET = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_VALIDITE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDITE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_VALIDITE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDITE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Mock
    private InscriptionRepository inscriptionRepositoryMock;

    @Autowired
    private InscriptionMapper inscriptionMapper;

    @Mock
    private InscriptionService inscriptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .objet(DEFAULT_OBJET)
            .dateValiditeDebut(DEFAULT_DATE_VALIDITE_DEBUT)
            .dateValiditeFin(DEFAULT_DATE_VALIDITE_FIN);
        // Add required entity
        Subscriber subscriber;
        if (TestUtil.findAll(em, Subscriber.class).isEmpty()) {
            subscriber = SubscriberResourceIT.createEntity(em);
            em.persist(subscriber);
            em.flush();
        } else {
            subscriber = TestUtil.findAll(em, Subscriber.class).get(0);
        }
        inscription.getSubscribers().add(subscriber);
        // Add required entity
        Formation formation;
        if (TestUtil.findAll(em, Formation.class).isEmpty()) {
            formation = FormationResourceIT.createEntity(em);
            em.persist(formation);
            em.flush();
        } else {
            formation = TestUtil.findAll(em, Formation.class).get(0);
        }
        inscription.setFormation(formation);
        return inscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createUpdatedEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .objet(UPDATED_OBJET)
            .dateValiditeDebut(UPDATED_DATE_VALIDITE_DEBUT)
            .dateValiditeFin(UPDATED_DATE_VALIDITE_FIN);
        // Add required entity
        Subscriber subscriber;
        if (TestUtil.findAll(em, Subscriber.class).isEmpty()) {
            subscriber = SubscriberResourceIT.createUpdatedEntity(em);
            em.persist(subscriber);
            em.flush();
        } else {
            subscriber = TestUtil.findAll(em, Subscriber.class).get(0);
        }
        inscription.getSubscribers().add(subscriber);
        // Add required entity
        Formation formation;
        if (TestUtil.findAll(em, Formation.class).isEmpty()) {
            formation = FormationResourceIT.createUpdatedEntity(em);
            em.persist(formation);
            em.flush();
        } else {
            formation = TestUtil.findAll(em, Formation.class).get(0);
        }
        inscription.setFormation(formation);
        return inscription;
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();
        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);
        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getObjet()).isEqualTo(DEFAULT_OBJET);
        assertThat(testInscription.getDateValiditeDebut()).isEqualTo(DEFAULT_DATE_VALIDITE_DEBUT);
        assertThat(testInscription.getDateValiditeFin()).isEqualTo(DEFAULT_DATE_VALIDITE_FIN);
    }

    @Test
    @Transactional
    void createInscriptionWithExistingId() throws Exception {
        // Create the Inscription with an existing ID
        inscription.setId(1L);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkObjetIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setObjet(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateValiditeDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setDateValiditeDebut(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateValiditeFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setDateValiditeFin(null);

        // Create the Inscription, which fails.
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        restInscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].objet").value(hasItem(DEFAULT_OBJET)))
            .andExpect(jsonPath("$.[*].dateValiditeDebut").value(hasItem(DEFAULT_DATE_VALIDITE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateValiditeFin").value(hasItem(DEFAULT_DATE_VALIDITE_FIN.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscriptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(inscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inscriptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInscriptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inscriptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInscriptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inscriptionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.objet").value(DEFAULT_OBJET))
            .andExpect(jsonPath("$.dateValiditeDebut").value(DEFAULT_DATE_VALIDITE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateValiditeFin").value(DEFAULT_DATE_VALIDITE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).get();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);
        updatedInscription.objet(UPDATED_OBJET).dateValiditeDebut(UPDATED_DATE_VALIDITE_DEBUT).dateValiditeFin(UPDATED_DATE_VALIDITE_FIN);
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(updatedInscription);

        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testInscription.getDateValiditeDebut()).isEqualTo(UPDATED_DATE_VALIDITE_DEBUT);
        assertThat(testInscription.getDateValiditeFin()).isEqualTo(UPDATED_DATE_VALIDITE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription.objet(UPDATED_OBJET).dateValiditeFin(UPDATED_DATE_VALIDITE_FIN);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testInscription.getDateValiditeDebut()).isEqualTo(DEFAULT_DATE_VALIDITE_DEBUT);
        assertThat(testInscription.getDateValiditeFin()).isEqualTo(UPDATED_DATE_VALIDITE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .objet(UPDATED_OBJET)
            .dateValiditeDebut(UPDATED_DATE_VALIDITE_DEBUT)
            .dateValiditeFin(UPDATED_DATE_VALIDITE_FIN);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getObjet()).isEqualTo(UPDATED_OBJET);
        assertThat(testInscription.getDateValiditeDebut()).isEqualTo(UPDATED_DATE_VALIDITE_DEBUT);
        assertThat(testInscription.getDateValiditeFin()).isEqualTo(UPDATED_DATE_VALIDITE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // Create the Inscription
        InscriptionDTO inscriptionDTO = inscriptionMapper.toDto(inscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        // Delete the inscription
        restInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
