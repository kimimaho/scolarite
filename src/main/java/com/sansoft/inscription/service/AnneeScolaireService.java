package com.sansoft.inscription.service;

import com.sansoft.inscription.domain.AnneeScolaire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link AnneeScolaire}.
 */
public interface AnneeScolaireService {

    /**
     * Save a anneeScolaire.
     *
     * @param anneeScolaire the entity to save.
     * @return the persisted entity.
     */
    AnneeScolaire save(AnneeScolaire anneeScolaire);

    /**
     * Get all the anneeScolaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnneeScolaire> findAll(Pageable pageable);


    /**
     * Get the "id" anneeScolaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnneeScolaire> findOne(Long id);

    /**
     * Delete the "id" anneeScolaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the anneeScolaire corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnneeScolaire> search(String query, Pageable pageable);
}
