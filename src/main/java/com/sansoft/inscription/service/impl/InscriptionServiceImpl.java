package com.sansoft.inscription.service.impl;

import com.sansoft.inscription.service.InscriptionService;
import com.sansoft.inscription.domain.Inscription;
import com.sansoft.inscription.repository.InscriptionRepository;
import com.sansoft.inscription.repository.search.InscriptionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Inscription}.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionServiceImpl.class);

    private final InscriptionRepository inscriptionRepository;

    private final InscriptionSearchRepository inscriptionSearchRepository;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository, InscriptionSearchRepository inscriptionSearchRepository) {
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionSearchRepository = inscriptionSearchRepository;
    }

    @Override
    public Inscription save(Inscription inscription) {
        log.debug("Request to save Inscription : {}", inscription);
        Inscription result = inscriptionRepository.save(inscription);
        inscriptionSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inscription> findAll(Pageable pageable) {
        log.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Inscription> findOne(Long id) {
        log.debug("Request to get Inscription : {}", id);
        return inscriptionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
        inscriptionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inscription> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Inscriptions for query {}", query);
        return inscriptionSearchRepository.search(queryStringQuery(query), pageable);    }
}
