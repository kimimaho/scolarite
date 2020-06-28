package com.sansoft.inscription.service;

import com.sansoft.inscription.domain.Niveau;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Niveau}.
 */
public interface NiveauService {

    /**
     * Save a niveau.
     *
     * @param niveau the entity to save.
     * @return the persisted entity.
     */
    Niveau save(Niveau niveau);

    /**
     * Get all the niveaus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Niveau> findAll(Pageable pageable);


    /**
     * Get the "id" niveau.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Niveau> findOne(Long id);

    /**
     * Delete the "id" niveau.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the niveau corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Niveau> search(String query, Pageable pageable);
}
