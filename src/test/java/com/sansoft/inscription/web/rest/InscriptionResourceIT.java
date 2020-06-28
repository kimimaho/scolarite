package com.sansoft.inscription.web.rest;

import com.sansoft.inscription.ApplicationScolariteApp;
import com.sansoft.inscription.domain.Inscription;
import com.sansoft.inscription.domain.Eleve;
import com.sansoft.inscription.domain.Niveau;
import com.sansoft.inscription.domain.AnneeScolaire;
import com.sansoft.inscription.repository.InscriptionRepository;
import com.sansoft.inscription.repository.search.InscriptionSearchRepository;
import com.sansoft.inscription.service.InscriptionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@SpringBootTest(classes = ApplicationScolariteApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InscriptionResourceIT {

    private static final LocalDate DEFAULT_DATE_INSCRIPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INSCRIPTION = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private InscriptionService inscriptionService;

    /**
     * This repository is mocked in the com.sansoft.inscription.repository.search test package.
     *
     * @see com.sansoft.inscription.repository.search.InscriptionSearchRepositoryMockConfiguration
     */
    @Autowired
    private InscriptionSearchRepository mockInscriptionSearchRepository;

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
            .dateInscription(DEFAULT_DATE_INSCRIPTION);
        // Add required entity
        Eleve eleve;
        if (TestUtil.findAll(em, Eleve.class).isEmpty()) {
            eleve = EleveResourceIT.createEntity(em);
            em.persist(eleve);
            em.flush();
        } else {
            eleve = TestUtil.findAll(em, Eleve.class).get(0);
        }
        inscription.setEleve(eleve);
        // Add required entity
        Niveau niveau;
        if (TestUtil.findAll(em, Niveau.class).isEmpty()) {
            niveau = NiveauResourceIT.createEntity(em);
            em.persist(niveau);
            em.flush();
        } else {
            niveau = TestUtil.findAll(em, Niveau.class).get(0);
        }
        inscription.setNiveau(niveau);
        // Add required entity
        AnneeScolaire anneeScolaire;
        if (TestUtil.findAll(em, AnneeScolaire.class).isEmpty()) {
            anneeScolaire = AnneeScolaireResourceIT.createEntity(em);
            em.persist(anneeScolaire);
            em.flush();
        } else {
            anneeScolaire = TestUtil.findAll(em, AnneeScolaire.class).get(0);
        }
        inscription.setAnneeScolaire(anneeScolaire);
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
            .dateInscription(UPDATED_DATE_INSCRIPTION);
        // Add required entity
        Eleve eleve;
        if (TestUtil.findAll(em, Eleve.class).isEmpty()) {
            eleve = EleveResourceIT.createUpdatedEntity(em);
            em.persist(eleve);
            em.flush();
        } else {
            eleve = TestUtil.findAll(em, Eleve.class).get(0);
        }
        inscription.setEleve(eleve);
        // Add required entity
        Niveau niveau;
        if (TestUtil.findAll(em, Niveau.class).isEmpty()) {
            niveau = NiveauResourceIT.createUpdatedEntity(em);
            em.persist(niveau);
            em.flush();
        } else {
            niveau = TestUtil.findAll(em, Niveau.class).get(0);
        }
        inscription.setNiveau(niveau);
        // Add required entity
        AnneeScolaire anneeScolaire;
        if (TestUtil.findAll(em, AnneeScolaire.class).isEmpty()) {
            anneeScolaire = AnneeScolaireResourceIT.createUpdatedEntity(em);
            em.persist(anneeScolaire);
            em.flush();
        } else {
            anneeScolaire = TestUtil.findAll(em, AnneeScolaire.class).get(0);
        }
        inscription.setAnneeScolaire(anneeScolaire);
        return inscription;
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    public void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();
        // Create the Inscription
        restInscriptionMockMvc.perform(post("/api/inscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isCreated());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(DEFAULT_DATE_INSCRIPTION);

        // Validate the Inscription in Elasticsearch
        verify(mockInscriptionSearchRepository, times(1)).save(testInscription);
    }

    @Test
    @Transactional
    public void createInscriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        // Create the Inscription with an existing ID
        inscription.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc.perform(post("/api/inscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Inscription in Elasticsearch
        verify(mockInscriptionSearchRepository, times(0)).save(inscription);
    }


    @Test
    @Transactional
    public void checkDateInscriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = inscriptionRepository.findAll().size();
        // set the field null
        inscription.setDateInscription(null);

        // Create the Inscription, which fails.


        restInscriptionMockMvc.perform(post("/api/inscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isBadRequest());

        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc.perform(get("/api/inscriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get("/api/inscriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscription() throws Exception {
        // Initialize the database
        inscriptionService.save(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).get();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);
        updatedInscription
            .dateInscription(UPDATED_DATE_INSCRIPTION);

        restInscriptionMockMvc.perform(put("/api/inscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInscription)))
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);

        // Validate the Inscription in Elasticsearch
        verify(mockInscriptionSearchRepository, times(2)).save(testInscription);
    }

    @Test
    @Transactional
    public void updateNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc.perform(put("/api/inscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Inscription in Elasticsearch
        verify(mockInscriptionSearchRepository, times(0)).save(inscription);
    }

    @Test
    @Transactional
    public void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionService.save(inscription);

        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        // Delete the inscription
        restInscriptionMockMvc.perform(delete("/api/inscriptions/{id}", inscription.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Inscription in Elasticsearch
        verify(mockInscriptionSearchRepository, times(1)).deleteById(inscription.getId());
    }

    @Test
    @Transactional
    public void searchInscription() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        inscriptionService.save(inscription);
        when(mockInscriptionSearchRepository.search(queryStringQuery("id:" + inscription.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(inscription), PageRequest.of(0, 1), 1));

        // Search the inscription
        restInscriptionMockMvc.perform(get("/api/_search/inscriptions?query=id:" + inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())));
    }
}
