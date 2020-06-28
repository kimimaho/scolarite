package com.sansoft.inscription.web.rest;

import com.sansoft.inscription.domain.Scolarite;
import com.sansoft.inscription.repository.ScolariteRepository;
import com.sansoft.inscription.repository.search.ScolariteSearchRepository;
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
 * REST controller for managing {@link com.sansoft.inscription.domain.Scolarite}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ScolariteResource {

    private final Logger log = LoggerFactory.getLogger(ScolariteResource.class);

    private static final String ENTITY_NAME = "applicationScolariteScolarite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScolariteRepository scolariteRepository;

    private final ScolariteSearchRepository scolariteSearchRepository;

    public ScolariteResource(ScolariteRepository scolariteRepository, ScolariteSearchRepository scolariteSearchRepository) {
        this.scolariteRepository = scolariteRepository;
        this.scolariteSearchRepository = scolariteSearchRepository;
    }

    /**
     * {@code POST  /scolarites} : Create a new scolarite.
     *
     * @param scolarite the scolarite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scolarite, or with status {@code 400 (Bad Request)} if the scolarite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scolarites")
    public ResponseEntity<Scolarite> createScolarite(@Valid @RequestBody Scolarite scolarite) throws URISyntaxException {
        log.debug("REST request to save Scolarite : {}", scolarite);
        if (scolarite.getId() != null) {
            throw new BadRequestAlertException("A new scolarite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scolarite result = scolariteRepository.save(scolarite);
        scolariteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/scolarites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scolarites} : Updates an existing scolarite.
     *
     * @param scolarite the scolarite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scolarite,
     * or with status {@code 400 (Bad Request)} if the scolarite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scolarite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scolarites")
    public ResponseEntity<Scolarite> updateScolarite(@Valid @RequestBody Scolarite scolarite) throws URISyntaxException {
        log.debug("REST request to update Scolarite : {}", scolarite);
        if (scolarite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Scolarite result = scolariteRepository.save(scolarite);
        scolariteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scolarite.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /scolarites} : get all the scolarites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scolarites in body.
     */
    @GetMapping("/scolarites")
    public ResponseEntity<List<Scolarite>> getAllScolarites(Pageable pageable) {
        log.debug("REST request to get a page of Scolarites");
        Page<Scolarite> page = scolariteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scolarites/:id} : get the "id" scolarite.
     *
     * @param id the id of the scolarite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scolarite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scolarites/{id}")
    public ResponseEntity<Scolarite> getScolarite(@PathVariable Long id) {
        log.debug("REST request to get Scolarite : {}", id);
        Optional<Scolarite> scolarite = scolariteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(scolarite);
    }

    /**
     * {@code DELETE  /scolarites/:id} : delete the "id" scolarite.
     *
     * @param id the id of the scolarite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scolarites/{id}")
    public ResponseEntity<Void> deleteScolarite(@PathVariable Long id) {
        log.debug("REST request to delete Scolarite : {}", id);
        scolariteRepository.deleteById(id);
        scolariteSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/scolarites?query=:query} : search for the scolarite corresponding
     * to the query.
     *
     * @param query the query of the scolarite search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/scolarites")
    public ResponseEntity<List<Scolarite>> searchScolarites(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Scolarites for query {}", query);
        Page<Scolarite> page = scolariteSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
