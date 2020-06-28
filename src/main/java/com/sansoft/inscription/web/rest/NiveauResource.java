package com.sansoft.inscription.web.rest;

import com.sansoft.inscription.domain.Niveau;
import com.sansoft.inscription.service.NiveauService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.sansoft.inscription.domain.Niveau}.
 */
@RestController
@RequestMapping("/api")
public class NiveauResource {

    private final Logger log = LoggerFactory.getLogger(NiveauResource.class);

    private static final String ENTITY_NAME = "applicationScolariteNiveau";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NiveauService niveauService;

    public NiveauResource(NiveauService niveauService) {
        this.niveauService = niveauService;
    }

    /**
     * {@code POST  /niveaus} : Create a new niveau.
     *
     * @param niveau the niveau to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new niveau, or with status {@code 400 (Bad Request)} if the niveau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/niveaus")
    public ResponseEntity<Niveau> createNiveau(@Valid @RequestBody Niveau niveau) throws URISyntaxException {
        log.debug("REST request to save Niveau : {}", niveau);
        if (niveau.getId() != null) {
            throw new BadRequestAlertException("A new niveau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Niveau result = niveauService.save(niveau);
        return ResponseEntity.created(new URI("/api/niveaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /niveaus} : Updates an existing niveau.
     *
     * @param niveau the niveau to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated niveau,
     * or with status {@code 400 (Bad Request)} if the niveau is not valid,
     * or with status {@code 500 (Internal Server Error)} if the niveau couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/niveaus")
    public ResponseEntity<Niveau> updateNiveau(@Valid @RequestBody Niveau niveau) throws URISyntaxException {
        log.debug("REST request to update Niveau : {}", niveau);
        if (niveau.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Niveau result = niveauService.save(niveau);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, niveau.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /niveaus} : get all the niveaus.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of niveaus in body.
     */
    @GetMapping("/niveaus")
    public ResponseEntity<List<Niveau>> getAllNiveaus(Pageable pageable) {
        log.debug("REST request to get a page of Niveaus");
        Page<Niveau> page = niveauService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /niveaus/:id} : get the "id" niveau.
     *
     * @param id the id of the niveau to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the niveau, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/niveaus/{id}")
    public ResponseEntity<Niveau> getNiveau(@PathVariable Long id) {
        log.debug("REST request to get Niveau : {}", id);
        Optional<Niveau> niveau = niveauService.findOne(id);
        return ResponseUtil.wrapOrNotFound(niveau);
    }

    /**
     * {@code DELETE  /niveaus/:id} : delete the "id" niveau.
     *
     * @param id the id of the niveau to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/niveaus/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Long id) {
        log.debug("REST request to delete Niveau : {}", id);
        niveauService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/niveaus?query=:query} : search for the niveau corresponding
     * to the query.
     *
     * @param query the query of the niveau search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/niveaus")
    public ResponseEntity<List<Niveau>> searchNiveaus(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Niveaus for query {}", query);
        Page<Niveau> page = niveauService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
