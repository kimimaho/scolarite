package com.sansoft.inscription.web.rest;

import com.sansoft.inscription.ApplicationScolariteApp;
import com.sansoft.inscription.domain.AnneeScolaire;
import com.sansoft.inscription.repository.AnneeScolaireRepository;
import com.sansoft.inscription.repository.search.AnneeScolaireSearchRepository;
import com.sansoft.inscription.service.AnneeScolaireService;

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
 * Integration tests for the {@link AnneeScolaireResource} REST controller.
 */
@SpringBootTest(classes = ApplicationScolariteApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnneeScolaireResourceIT {

    private static final Integer DEFAULT_ANNEE = 1;
    private static final Integer UPDATED_ANNEE = 2;

    @Autowired
    private AnneeScolaireRepository anneeScolaireRepository;

    @Autowired
    private AnneeScolaireService anneeScolaireService;

    /**
     * This repository is mocked in the com.sansoft.inscription.repository.search test package.
     *
     * @see com.sansoft.inscription.repository.search.AnneeScolaireSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnneeScolaireSearchRepository mockAnneeScolaireSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnneeScolaireMockMvc;

    private AnneeScolaire anneeScolaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeScolaire createEntity(EntityManager em) {
        AnneeScolaire anneeScolaire = new AnneeScolaire()
            .annee(DEFAULT_ANNEE);
        return anneeScolaire;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnneeScolaire createUpdatedEntity(EntityManager em) {
        AnneeScolaire anneeScolaire = new AnneeScolaire()
            .annee(UPDATED_ANNEE);
        return anneeScolaire;
    }

    @BeforeEach
    public void initTest() {
        anneeScolaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnneeScolaire() throws Exception {
        int databaseSizeBeforeCreate = anneeScolaireRepository.findAll().size();
        // Create the AnneeScolaire
        restAnneeScolaireMockMvc.perform(post("/api/annee-scolaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeScolaire)))
            .andExpect(status().isCreated());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeCreate + 1);
        AnneeScolaire testAnneeScolaire = anneeScolaireList.get(anneeScolaireList.size() - 1);
        assertThat(testAnneeScolaire.getAnnee()).isEqualTo(DEFAULT_ANNEE);

        // Validate the AnneeScolaire in Elasticsearch
        verify(mockAnneeScolaireSearchRepository, times(1)).save(testAnneeScolaire);
    }

    @Test
    @Transactional
    public void createAnneeScolaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = anneeScolaireRepository.findAll().size();

        // Create the AnneeScolaire with an existing ID
        anneeScolaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnneeScolaireMockMvc.perform(post("/api/annee-scolaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeScolaire)))
            .andExpect(status().isBadRequest());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeCreate);

        // Validate the AnneeScolaire in Elasticsearch
        verify(mockAnneeScolaireSearchRepository, times(0)).save(anneeScolaire);
    }


    @Test
    @Transactional
    public void checkAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = anneeScolaireRepository.findAll().size();
        // set the field null
        anneeScolaire.setAnnee(null);

        // Create the AnneeScolaire, which fails.


        restAnneeScolaireMockMvc.perform(post("/api/annee-scolaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeScolaire)))
            .andExpect(status().isBadRequest());

        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnneeScolaires() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        // Get all the anneeScolaireList
        restAnneeScolaireMockMvc.perform(get("/api/annee-scolaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anneeScolaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)));
    }
    
    @Test
    @Transactional
    public void getAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireRepository.saveAndFlush(anneeScolaire);

        // Get the anneeScolaire
        restAnneeScolaireMockMvc.perform(get("/api/annee-scolaires/{id}", anneeScolaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(anneeScolaire.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE));
    }
    @Test
    @Transactional
    public void getNonExistingAnneeScolaire() throws Exception {
        // Get the anneeScolaire
        restAnneeScolaireMockMvc.perform(get("/api/annee-scolaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireService.save(anneeScolaire);

        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();

        // Update the anneeScolaire
        AnneeScolaire updatedAnneeScolaire = anneeScolaireRepository.findById(anneeScolaire.getId()).get();
        // Disconnect from session so that the updates on updatedAnneeScolaire are not directly saved in db
        em.detach(updatedAnneeScolaire);
        updatedAnneeScolaire
            .annee(UPDATED_ANNEE);

        restAnneeScolaireMockMvc.perform(put("/api/annee-scolaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnneeScolaire)))
            .andExpect(status().isOk());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);
        AnneeScolaire testAnneeScolaire = anneeScolaireList.get(anneeScolaireList.size() - 1);
        assertThat(testAnneeScolaire.getAnnee()).isEqualTo(UPDATED_ANNEE);

        // Validate the AnneeScolaire in Elasticsearch
        verify(mockAnneeScolaireSearchRepository, times(2)).save(testAnneeScolaire);
    }

    @Test
    @Transactional
    public void updateNonExistingAnneeScolaire() throws Exception {
        int databaseSizeBeforeUpdate = anneeScolaireRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnneeScolaireMockMvc.perform(put("/api/annee-scolaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(anneeScolaire)))
            .andExpect(status().isBadRequest());

        // Validate the AnneeScolaire in the database
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AnneeScolaire in Elasticsearch
        verify(mockAnneeScolaireSearchRepository, times(0)).save(anneeScolaire);
    }

    @Test
    @Transactional
    public void deleteAnneeScolaire() throws Exception {
        // Initialize the database
        anneeScolaireService.save(anneeScolaire);

        int databaseSizeBeforeDelete = anneeScolaireRepository.findAll().size();

        // Delete the anneeScolaire
        restAnneeScolaireMockMvc.perform(delete("/api/annee-scolaires/{id}", anneeScolaire.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnneeScolaire> anneeScolaireList = anneeScolaireRepository.findAll();
        assertThat(anneeScolaireList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AnneeScolaire in Elasticsearch
        verify(mockAnneeScolaireSearchRepository, times(1)).deleteById(anneeScolaire.getId());
    }

    @Test
    @Transactional
    public void searchAnneeScolaire() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        anneeScolaireService.save(anneeScolaire);
        when(mockAnneeScolaireSearchRepository.search(queryStringQuery("id:" + anneeScolaire.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(anneeScolaire), PageRequest.of(0, 1), 1));

        // Search the anneeScolaire
        restAnneeScolaireMockMvc.perform(get("/api/_search/annee-scolaires?query=id:" + anneeScolaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anneeScolaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE)));
    }
}
