package com.sansoft.inscription.web.rest;

import com.sansoft.inscription.ApplicationScolariteApp;
import com.sansoft.inscription.domain.Niveau;
import com.sansoft.inscription.repository.NiveauRepository;
import com.sansoft.inscription.repository.search.NiveauSearchRepository;
import com.sansoft.inscription.service.NiveauService;

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
 * Integration tests for the {@link NiveauResource} REST controller.
 */
@SpringBootTest(classes = ApplicationScolariteApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class NiveauResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    @Autowired
    private NiveauRepository niveauRepository;

    @Autowired
    private NiveauService niveauService;

    /**
     * This repository is mocked in the com.sansoft.inscription.repository.search test package.
     *
     * @see com.sansoft.inscription.repository.search.NiveauSearchRepositoryMockConfiguration
     */
    @Autowired
    private NiveauSearchRepository mockNiveauSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNiveauMockMvc;

    private Niveau niveau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Niveau createEntity(EntityManager em) {
        Niveau niveau = new Niveau()
            .intitule(DEFAULT_INTITULE);
        return niveau;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Niveau createUpdatedEntity(EntityManager em) {
        Niveau niveau = new Niveau()
            .intitule(UPDATED_INTITULE);
        return niveau;
    }

    @BeforeEach
    public void initTest() {
        niveau = createEntity(em);
    }

    @Test
    @Transactional
    public void createNiveau() throws Exception {
        int databaseSizeBeforeCreate = niveauRepository.findAll().size();
        // Create the Niveau
        restNiveauMockMvc.perform(post("/api/niveaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(niveau)))
            .andExpect(status().isCreated());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeCreate + 1);
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getIntitule()).isEqualTo(DEFAULT_INTITULE);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(1)).save(testNiveau);
    }

    @Test
    @Transactional
    public void createNiveauWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = niveauRepository.findAll().size();

        // Create the Niveau with an existing ID
        niveau.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNiveauMockMvc.perform(post("/api/niveaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(niveau)))
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeCreate);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(0)).save(niveau);
    }


    @Test
    @Transactional
    public void checkIntituleIsRequired() throws Exception {
        int databaseSizeBeforeTest = niveauRepository.findAll().size();
        // set the field null
        niveau.setIntitule(null);

        // Create the Niveau, which fails.


        restNiveauMockMvc.perform(post("/api/niveaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(niveau)))
            .andExpect(status().isBadRequest());

        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNiveaus() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get all the niveauList
        restNiveauMockMvc.perform(get("/api/niveaus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)));
    }
    
    @Test
    @Transactional
    public void getNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get the niveau
        restNiveauMockMvc.perform(get("/api/niveaus/{id}", niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(niveau.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE));
    }
    @Test
    @Transactional
    public void getNonExistingNiveau() throws Exception {
        // Get the niveau
        restNiveauMockMvc.perform(get("/api/niveaus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNiveau() throws Exception {
        // Initialize the database
        niveauService.save(niveau);

        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();

        // Update the niveau
        Niveau updatedNiveau = niveauRepository.findById(niveau.getId()).get();
        // Disconnect from session so that the updates on updatedNiveau are not directly saved in db
        em.detach(updatedNiveau);
        updatedNiveau
            .intitule(UPDATED_INTITULE);

        restNiveauMockMvc.perform(put("/api/niveaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNiveau)))
            .andExpect(status().isOk());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getIntitule()).isEqualTo(UPDATED_INTITULE);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(2)).save(testNiveau);
    }

    @Test
    @Transactional
    public void updateNonExistingNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauMockMvc.perform(put("/api/niveaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(niveau)))
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(0)).save(niveau);
    }

    @Test
    @Transactional
    public void deleteNiveau() throws Exception {
        // Initialize the database
        niveauService.save(niveau);

        int databaseSizeBeforeDelete = niveauRepository.findAll().size();

        // Delete the niveau
        restNiveauMockMvc.perform(delete("/api/niveaus/{id}", niveau.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(1)).deleteById(niveau.getId());
    }

    @Test
    @Transactional
    public void searchNiveau() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        niveauService.save(niveau);
        when(mockNiveauSearchRepository.search(queryStringQuery("id:" + niveau.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(niveau), PageRequest.of(0, 1), 1));

        // Search the niveau
        restNiveauMockMvc.perform(get("/api/_search/niveaus?query=id:" + niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)));
    }
}
