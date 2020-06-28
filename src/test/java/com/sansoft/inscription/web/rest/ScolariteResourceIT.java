package com.sansoft.inscription.web.rest;

import com.sansoft.inscription.ApplicationScolariteApp;
import com.sansoft.inscription.domain.Scolarite;
import com.sansoft.inscription.domain.Niveau;
import com.sansoft.inscription.domain.AnneeScolaire;
import com.sansoft.inscription.repository.ScolariteRepository;
import com.sansoft.inscription.repository.search.ScolariteSearchRepository;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ScolariteResource} REST controller.
 */
@SpringBootTest(classes = ApplicationScolariteApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ScolariteResourceIT {

    private static final Integer DEFAULT_EFFECTIF = 1;
    private static final Integer UPDATED_EFFECTIF = 2;

    private static final Float DEFAULT_SCOLARITE = 1F;
    private static final Float UPDATED_SCOLARITE = 2F;

    @Autowired
    private ScolariteRepository scolariteRepository;

    /**
     * This repository is mocked in the com.sansoft.inscription.repository.search test package.
     *
     * @see com.sansoft.inscription.repository.search.ScolariteSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScolariteSearchRepository mockScolariteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScolariteMockMvc;

    private Scolarite scolarite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scolarite createEntity(EntityManager em) {
        Scolarite scolarite = new Scolarite()
            .effectif(DEFAULT_EFFECTIF)
            .scolarite(DEFAULT_SCOLARITE);
        // Add required entity
        Niveau niveau;
        if (TestUtil.findAll(em, Niveau.class).isEmpty()) {
            niveau = NiveauResourceIT.createEntity(em);
            em.persist(niveau);
            em.flush();
        } else {
            niveau = TestUtil.findAll(em, Niveau.class).get(0);
        }
        scolarite.setNiveau(niveau);
        // Add required entity
        AnneeScolaire anneeScolaire;
        if (TestUtil.findAll(em, AnneeScolaire.class).isEmpty()) {
            anneeScolaire = AnneeScolaireResourceIT.createEntity(em);
            em.persist(anneeScolaire);
            em.flush();
        } else {
            anneeScolaire = TestUtil.findAll(em, AnneeScolaire.class).get(0);
        }
        scolarite.setAnneeScolaire(anneeScolaire);
        return scolarite;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scolarite createUpdatedEntity(EntityManager em) {
        Scolarite scolarite = new Scolarite()
            .effectif(UPDATED_EFFECTIF)
            .scolarite(UPDATED_SCOLARITE);
        // Add required entity
        Niveau niveau;
        if (TestUtil.findAll(em, Niveau.class).isEmpty()) {
            niveau = NiveauResourceIT.createUpdatedEntity(em);
            em.persist(niveau);
            em.flush();
        } else {
            niveau = TestUtil.findAll(em, Niveau.class).get(0);
        }
        scolarite.setNiveau(niveau);
        // Add required entity
        AnneeScolaire anneeScolaire;
        if (TestUtil.findAll(em, AnneeScolaire.class).isEmpty()) {
            anneeScolaire = AnneeScolaireResourceIT.createUpdatedEntity(em);
            em.persist(anneeScolaire);
            em.flush();
        } else {
            anneeScolaire = TestUtil.findAll(em, AnneeScolaire.class).get(0);
        }
        scolarite.setAnneeScolaire(anneeScolaire);
        return scolarite;
    }

    @BeforeEach
    public void initTest() {
        scolarite = createEntity(em);
    }

    @Test
    @Transactional
    public void createScolarite() throws Exception {
        int databaseSizeBeforeCreate = scolariteRepository.findAll().size();
        // Create the Scolarite
        restScolariteMockMvc.perform(post("/api/scolarites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(scolarite)))
            .andExpect(status().isCreated());

        // Validate the Scolarite in the database
        List<Scolarite> scolariteList = scolariteRepository.findAll();
        assertThat(scolariteList).hasSize(databaseSizeBeforeCreate + 1);
        Scolarite testScolarite = scolariteList.get(scolariteList.size() - 1);
        assertThat(testScolarite.getEffectif()).isEqualTo(DEFAULT_EFFECTIF);
        assertThat(testScolarite.getScolarite()).isEqualTo(DEFAULT_SCOLARITE);

        // Validate the Scolarite in Elasticsearch
        verify(mockScolariteSearchRepository, times(1)).save(testScolarite);
    }

    @Test
    @Transactional
    public void createScolariteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scolariteRepository.findAll().size();

        // Create the Scolarite with an existing ID
        scolarite.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScolariteMockMvc.perform(post("/api/scolarites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(scolarite)))
            .andExpect(status().isBadRequest());

        // Validate the Scolarite in the database
        List<Scolarite> scolariteList = scolariteRepository.findAll();
        assertThat(scolariteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Scolarite in Elasticsearch
        verify(mockScolariteSearchRepository, times(0)).save(scolarite);
    }


    @Test
    @Transactional
    public void checkEffectifIsRequired() throws Exception {
        int databaseSizeBeforeTest = scolariteRepository.findAll().size();
        // set the field null
        scolarite.setEffectif(null);

        // Create the Scolarite, which fails.


        restScolariteMockMvc.perform(post("/api/scolarites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(scolarite)))
            .andExpect(status().isBadRequest());

        List<Scolarite> scolariteList = scolariteRepository.findAll();
        assertThat(scolariteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkScolariteIsRequired() throws Exception {
        int databaseSizeBeforeTest = scolariteRepository.findAll().size();
        // set the field null
        scolarite.setScolarite(null);

        // Create the Scolarite, which fails.


        restScolariteMockMvc.perform(post("/api/scolarites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(scolarite)))
            .andExpect(status().isBadRequest());

        List<Scolarite> scolariteList = scolariteRepository.findAll();
        assertThat(scolariteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScolarites() throws Exception {
        // Initialize the database
        scolariteRepository.saveAndFlush(scolarite);

        // Get all the scolariteList
        restScolariteMockMvc.perform(get("/api/scolarites?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scolarite.getId().intValue())))
            .andExpect(jsonPath("$.[*].effectif").value(hasItem(DEFAULT_EFFECTIF)))
            .andExpect(jsonPath("$.[*].scolarite").value(hasItem(DEFAULT_SCOLARITE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getScolarite() throws Exception {
        // Initialize the database
        scolariteRepository.saveAndFlush(scolarite);

        // Get the scolarite
        restScolariteMockMvc.perform(get("/api/scolarites/{id}", scolarite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scolarite.getId().intValue()))
            .andExpect(jsonPath("$.effectif").value(DEFAULT_EFFECTIF))
            .andExpect(jsonPath("$.scolarite").value(DEFAULT_SCOLARITE.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingScolarite() throws Exception {
        // Get the scolarite
        restScolariteMockMvc.perform(get("/api/scolarites/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScolarite() throws Exception {
        // Initialize the database
        scolariteRepository.saveAndFlush(scolarite);

        int databaseSizeBeforeUpdate = scolariteRepository.findAll().size();

        // Update the scolarite
        Scolarite updatedScolarite = scolariteRepository.findById(scolarite.getId()).get();
        // Disconnect from session so that the updates on updatedScolarite are not directly saved in db
        em.detach(updatedScolarite);
        updatedScolarite
            .effectif(UPDATED_EFFECTIF)
            .scolarite(UPDATED_SCOLARITE);

        restScolariteMockMvc.perform(put("/api/scolarites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedScolarite)))
            .andExpect(status().isOk());

        // Validate the Scolarite in the database
        List<Scolarite> scolariteList = scolariteRepository.findAll();
        assertThat(scolariteList).hasSize(databaseSizeBeforeUpdate);
        Scolarite testScolarite = scolariteList.get(scolariteList.size() - 1);
        assertThat(testScolarite.getEffectif()).isEqualTo(UPDATED_EFFECTIF);
        assertThat(testScolarite.getScolarite()).isEqualTo(UPDATED_SCOLARITE);

        // Validate the Scolarite in Elasticsearch
        verify(mockScolariteSearchRepository, times(1)).save(testScolarite);
    }

    @Test
    @Transactional
    public void updateNonExistingScolarite() throws Exception {
        int databaseSizeBeforeUpdate = scolariteRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScolariteMockMvc.perform(put("/api/scolarites")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(scolarite)))
            .andExpect(status().isBadRequest());

        // Validate the Scolarite in the database
        List<Scolarite> scolariteList = scolariteRepository.findAll();
        assertThat(scolariteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Scolarite in Elasticsearch
        verify(mockScolariteSearchRepository, times(0)).save(scolarite);
    }

    @Test
    @Transactional
    public void deleteScolarite() throws Exception {
        // Initialize the database
        scolariteRepository.saveAndFlush(scolarite);

        int databaseSizeBeforeDelete = scolariteRepository.findAll().size();

        // Delete the scolarite
        restScolariteMockMvc.perform(delete("/api/scolarites/{id}", scolarite.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Scolarite> scolariteList = scolariteRepository.findAll();
        assertThat(scolariteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Scolarite in Elasticsearch
        verify(mockScolariteSearchRepository, times(1)).deleteById(scolarite.getId());
    }

    @Test
    @Transactional
    public void searchScolarite() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        scolariteRepository.saveAndFlush(scolarite);
        when(mockScolariteSearchRepository.search(queryStringQuery("id:" + scolarite.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(scolarite), PageRequest.of(0, 1), 1));

        // Search the scolarite
        restScolariteMockMvc.perform(get("/api/_search/scolarites?query=id:" + scolarite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scolarite.getId().intValue())))
            .andExpect(jsonPath("$.[*].effectif").value(hasItem(DEFAULT_EFFECTIF)))
            .andExpect(jsonPath("$.[*].scolarite").value(hasItem(DEFAULT_SCOLARITE.doubleValue())));
    }
}
