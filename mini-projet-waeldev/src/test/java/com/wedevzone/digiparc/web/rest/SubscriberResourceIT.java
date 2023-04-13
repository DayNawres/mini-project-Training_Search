package com.wedevzone.digiparc.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wedevzone.digiparc.IntegrationTest;
import com.wedevzone.digiparc.domain.Formation;
import com.wedevzone.digiparc.domain.Subscriber;
import com.wedevzone.digiparc.repository.SubscriberRepository;
import com.wedevzone.digiparc.service.dto.SubscriberDTO;
import com.wedevzone.digiparc.service.mapper.SubscriberMapper;
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
 * Integration tests for the {@link SubscriberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubscriberResourceIT {

    private static final String DEFAULT_C_IN = "AAAAAAAAAA";
    private static final String UPDATED_C_IN = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subscribers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberMapper subscriberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubscriberMockMvc;

    private Subscriber subscriber;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriber createEntity(EntityManager em) {
        Subscriber subscriber = new Subscriber()
            .cIN(DEFAULT_C_IN)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .age(DEFAULT_AGE)
            .statut(DEFAULT_STATUT);
        // Add required entity
        Formation formation;
        if (TestUtil.findAll(em, Formation.class).isEmpty()) {
            formation = FormationResourceIT.createEntity(em);
            em.persist(formation);
            em.flush();
        } else {
            formation = TestUtil.findAll(em, Formation.class).get(0);
        }
        subscriber.getFormations().add(formation);
        return subscriber;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subscriber createUpdatedEntity(EntityManager em) {
        Subscriber subscriber = new Subscriber()
            .cIN(UPDATED_C_IN)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .age(UPDATED_AGE)
            .statut(UPDATED_STATUT);
        // Add required entity
        Formation formation;
        if (TestUtil.findAll(em, Formation.class).isEmpty()) {
            formation = FormationResourceIT.createUpdatedEntity(em);
            em.persist(formation);
            em.flush();
        } else {
            formation = TestUtil.findAll(em, Formation.class).get(0);
        }
        subscriber.getFormations().add(formation);
        return subscriber;
    }

    @BeforeEach
    public void initTest() {
        subscriber = createEntity(em);
    }

    @Test
    @Transactional
    void createSubscriber() throws Exception {
        int databaseSizeBeforeCreate = subscriberRepository.findAll().size();
        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);
        restSubscriberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isCreated());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate + 1);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getcIN()).isEqualTo(DEFAULT_C_IN);
        assertThat(testSubscriber.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testSubscriber.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testSubscriber.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testSubscriber.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void createSubscriberWithExistingId() throws Exception {
        // Create the Subscriber with an existing ID
        subscriber.setId(1L);
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        int databaseSizeBeforeCreate = subscriberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubscriberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkcINIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setcIN(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setNom(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setPrenom(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        int databaseSizeBeforeTest = subscriberRepository.findAll().size();
        // set the field null
        subscriber.setStatut(null);

        // Create the Subscriber, which fails.
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        restSubscriberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isBadRequest());

        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubscribers() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get all the subscriberList
        restSubscriberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subscriber.getId().intValue())))
            .andExpect(jsonPath("$.[*].cIN").value(hasItem(DEFAULT_C_IN)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)));
    }

    @Test
    @Transactional
    void getSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        // Get the subscriber
        restSubscriberMockMvc
            .perform(get(ENTITY_API_URL_ID, subscriber.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subscriber.getId().intValue()))
            .andExpect(jsonPath("$.cIN").value(DEFAULT_C_IN))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT));
    }

    @Test
    @Transactional
    void getNonExistingSubscriber() throws Exception {
        // Get the subscriber
        restSubscriberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();

        // Update the subscriber
        Subscriber updatedSubscriber = subscriberRepository.findById(subscriber.getId()).get();
        // Disconnect from session so that the updates on updatedSubscriber are not directly saved in db
        em.detach(updatedSubscriber);
        updatedSubscriber.cIN(UPDATED_C_IN).nom(UPDATED_NOM).prenom(UPDATED_PRENOM).age(UPDATED_AGE).statut(UPDATED_STATUT);
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(updatedSubscriber);

        restSubscriberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            )
            .andExpect(status().isOk());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getcIN()).isEqualTo(UPDATED_C_IN);
        assertThat(testSubscriber.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testSubscriber.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testSubscriber.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testSubscriber.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void putNonExistingSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();
        subscriber.setId(count.incrementAndGet());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subscriberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();
        subscriber.setId(count.incrementAndGet());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();
        subscriber.setId(count.incrementAndGet());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subscriberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubscriberWithPatch() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();

        // Update the subscriber using partial update
        Subscriber partialUpdatedSubscriber = new Subscriber();
        partialUpdatedSubscriber.setId(subscriber.getId());

        partialUpdatedSubscriber.cIN(UPDATED_C_IN).prenom(UPDATED_PRENOM).age(UPDATED_AGE);

        restSubscriberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriber))
            )
            .andExpect(status().isOk());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getcIN()).isEqualTo(UPDATED_C_IN);
        assertThat(testSubscriber.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testSubscriber.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testSubscriber.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testSubscriber.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void fullUpdateSubscriberWithPatch() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();

        // Update the subscriber using partial update
        Subscriber partialUpdatedSubscriber = new Subscriber();
        partialUpdatedSubscriber.setId(subscriber.getId());

        partialUpdatedSubscriber.cIN(UPDATED_C_IN).nom(UPDATED_NOM).prenom(UPDATED_PRENOM).age(UPDATED_AGE).statut(UPDATED_STATUT);

        restSubscriberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubscriber.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubscriber))
            )
            .andExpect(status().isOk());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
        Subscriber testSubscriber = subscriberList.get(subscriberList.size() - 1);
        assertThat(testSubscriber.getcIN()).isEqualTo(UPDATED_C_IN);
        assertThat(testSubscriber.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testSubscriber.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testSubscriber.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testSubscriber.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void patchNonExistingSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();
        subscriber.setId(count.incrementAndGet());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubscriberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subscriberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();
        subscriber.setId(count.incrementAndGet());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubscriber() throws Exception {
        int databaseSizeBeforeUpdate = subscriberRepository.findAll().size();
        subscriber.setId(count.incrementAndGet());

        // Create the Subscriber
        SubscriberDTO subscriberDTO = subscriberMapper.toDto(subscriber);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubscriberMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subscriberDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subscriber in the database
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubscriber() throws Exception {
        // Initialize the database
        subscriberRepository.saveAndFlush(subscriber);

        int databaseSizeBeforeDelete = subscriberRepository.findAll().size();

        // Delete the subscriber
        restSubscriberMockMvc
            .perform(delete(ENTITY_API_URL_ID, subscriber.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Subscriber> subscriberList = subscriberRepository.findAll();
        assertThat(subscriberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
