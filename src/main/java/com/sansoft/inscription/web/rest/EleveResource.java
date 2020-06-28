package com.sansoft.inscription.web.rest;

import com.sansoft.inscription.domain.Eleve;
import com.sansoft.inscription.repository.EleveRepository;
import com.sansoft.inscription.repository.search.EleveSearchRepository;
import com.sansoft.inscription.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.sansoft.inscription.domain.Eleve}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EleveResource {

    private final Logger log = LoggerFactory.getLogger(EleveResource.class);

    private static final String ENTITY_NAME = "applicationScolariteEleve";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EleveRepository eleveRepository;

    private final EleveSearchRepository eleveSearchRepository;

    public EleveResource(EleveRepository eleveRepository, EleveSearchRepository eleveSearchRepository) {
        this.eleveRepository = eleveRepository;
        this.eleveSearchRepository = eleveSearchRepository;
    }

    /**
     * {@code POST  /eleves} : Create a new eleve.
     *
     * @param eleve the eleve to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eleve, or with status {@code 400 (Bad Request)} if the eleve has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eleves")
    public ResponseEntity<Eleve> createEleve(@Valid @RequestBody Eleve eleve) throws URISyntaxException {
        log.debug("REST request to save Eleve : {}", eleve);
        if (eleve.getId() != null) {
            throw new BadRequestAlertException("A new eleve cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Eleve result = eleveRepository.save(eleve);
        eleveSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/eleves/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eleves} : Updates an existing eleve.
     *
     * @param eleve the eleve to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eleve,
     * or with status {@code 400 (Bad Request)} if the eleve is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eleve couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eleves")
    public ResponseEntity<Eleve> updateEleve(@Valid @RequestBody Eleve eleve) throws URISyntaxException {
        log.debug("REST request to update Eleve : {}", eleve);
        if (eleve.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Eleve result = eleveRepository.save(eleve);
        eleveSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eleve.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /eleves} : get all the eleves.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eleves in body.
     */
    @GetMapping("/eleves")
    public ResponseEntity<List<Eleve>> getAllEleves(Pageable pageable) {
        log.debug("REST request to get a page of Eleves");
        Page<Eleve> page = eleveRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /eleves/:id} : get the "id" eleve.
     *
     * @param id the id of the eleve to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eleve, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eleves/{id}")
    public ResponseEntity<Eleve> getEleve(@PathVariable Long id) {
        log.debug("REST request to get Eleve : {}", id);
        Optional<Eleve> eleve = eleveRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eleve);
    }

    /**
     * {@code DELETE  /eleves/:id} : delete the "id" eleve.
     *
     * @param id the id of the eleve to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eleves/{id}")
    public ResponseEntity<Void> deleteEleve(@PathVariable Long id) {
        log.debug("REST request to delete Eleve : {}", id);
        eleveRepository.deleteById(id);
        eleveSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/eleves?query=:query} : search for the eleve corresponding
     * to the query.
     *
     * @param query the query of the eleve search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/eleves")
    public ResponseEntity<List<Eleve>> searchEleves(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Eleves for query {}", query);
        Page<Eleve> page = eleveSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
