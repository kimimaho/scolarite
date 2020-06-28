package com.sansoft.inscription.service.impl;

import com.sansoft.inscription.service.NiveauService;
import com.sansoft.inscription.domain.Niveau;
import com.sansoft.inscription.repository.NiveauRepository;
import com.sansoft.inscription.repository.search.NiveauSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Niveau}.
 */
@Service
@Transactional
public class NiveauServiceImpl implements NiveauService {

    private final Logger log = LoggerFactory.getLogger(NiveauServiceImpl.class);

    private final NiveauRepository niveauRepository;

    private final NiveauSearchRepository niveauSearchRepository;

    public NiveauServiceImpl(NiveauRepository niveauRepository, NiveauSearchRepository niveauSearchRepository) {
        this.niveauRepository = niveauRepository;
        this.niveauSearchRepository = niveauSearchRepository;
    }

    @Override
    public Niveau save(Niveau niveau) {
        log.debug("Request to save Niveau : {}", niveau);
        Niveau result = niveauRepository.save(niveau);
        niveauSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Niveau> findAll(Pageable pageable) {
        log.debug("Request to get all Niveaus");
        return niveauRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Niveau> findOne(Long id) {
        log.debug("Request to get Niveau : {}", id);
        return niveauRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Niveau : {}", id);
        niveauRepository.deleteById(id);
        niveauSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Niveau> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Niveaus for query {}", query);
        return niveauSearchRepository.search(queryStringQuery(query), pageable);    }
}
