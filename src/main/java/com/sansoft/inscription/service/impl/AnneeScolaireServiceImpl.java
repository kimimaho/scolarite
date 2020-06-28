package com.sansoft.inscription.service.impl;

import com.sansoft.inscription.service.AnneeScolaireService;
import com.sansoft.inscription.domain.AnneeScolaire;
import com.sansoft.inscription.repository.AnneeScolaireRepository;
import com.sansoft.inscription.repository.search.AnneeScolaireSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link AnneeScolaire}.
 */
@Service
@Transactional
public class AnneeScolaireServiceImpl implements AnneeScolaireService {

    private final Logger log = LoggerFactory.getLogger(AnneeScolaireServiceImpl.class);

    private final AnneeScolaireRepository anneeScolaireRepository;

    private final AnneeScolaireSearchRepository anneeScolaireSearchRepository;

    public AnneeScolaireServiceImpl(AnneeScolaireRepository anneeScolaireRepository, AnneeScolaireSearchRepository anneeScolaireSearchRepository) {
        this.anneeScolaireRepository = anneeScolaireRepository;
        this.anneeScolaireSearchRepository = anneeScolaireSearchRepository;
    }

    /**
     * Save a anneeScolaire.
     *
     * @param anneeScolaire the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AnneeScolaire save(AnneeScolaire anneeScolaire) {
        log.debug("Request to save AnneeScolaire : {}", anneeScolaire);
        AnneeScolaire result = anneeScolaireRepository.save(anneeScolaire);
        anneeScolaireSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the anneeScolaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnneeScolaire> findAll(Pageable pageable) {
        log.debug("Request to get all AnneeScolaires");
        return anneeScolaireRepository.findAll(pageable);
    }


    /**
     * Get one anneeScolaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AnneeScolaire> findOne(Long id) {
        log.debug("Request to get AnneeScolaire : {}", id);
        return anneeScolaireRepository.findById(id);
    }

    /**
     * Delete the anneeScolaire by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnneeScolaire : {}", id);
        anneeScolaireRepository.deleteById(id);
        anneeScolaireSearchRepository.deleteById(id);
    }

    /**
     * Search for the anneeScolaire corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnneeScolaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AnneeScolaires for query {}", query);
        return anneeScolaireSearchRepository.search(queryStringQuery(query), pageable);    }
}
