package com.cev.futbol.web.rest;

import com.cev.futbol.domain.Presidente;
import com.cev.futbol.service.PresidenteService;
import com.cev.futbol.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.cev.futbol.domain.Presidente}.
 */
@RestController
@RequestMapping("/api")
public class PresidenteResource {

    private final Logger log = LoggerFactory.getLogger(PresidenteResource.class);

    private static final String ENTITY_NAME = "presidente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresidenteService presidenteService;

    public PresidenteResource(PresidenteService presidenteService) {
        this.presidenteService = presidenteService;
    }

    /**
     * {@code POST  /presidentes} : Create a new presidente.
     *
     * @param presidente the presidente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presidente, or with status {@code 400 (Bad Request)} if the presidente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presidentes")
    public ResponseEntity<Presidente> createPresidente(@Valid @RequestBody Presidente presidente) throws URISyntaxException {
        log.debug("REST request to save Presidente : {}", presidente);
        if (presidente.getId() != null) {
            throw new BadRequestAlertException("A new presidente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Presidente result = presidenteService.save(presidente);
        return ResponseEntity.created(new URI("/api/presidentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presidentes} : Updates an existing presidente.
     *
     * @param presidente the presidente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presidente,
     * or with status {@code 400 (Bad Request)} if the presidente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presidente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presidentes")
    public ResponseEntity<Presidente> updatePresidente(@Valid @RequestBody Presidente presidente) throws URISyntaxException {
        log.debug("REST request to update Presidente : {}", presidente);
        if (presidente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Presidente result = presidenteService.save(presidente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, presidente.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /presidentes} : get all the presidentes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presidentes in body.
     */
    @GetMapping("/presidentes")
    public ResponseEntity<List<Presidente>> getAllPresidentes(Pageable pageable) {
        log.debug("REST request to get a page of Presidentes");
        Page<Presidente> page = presidenteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /presidentes/:id} : get the "id" presidente.
     *
     * @param id the id of the presidente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presidente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presidentes/{id}")
    public ResponseEntity<Presidente> getPresidente(@PathVariable Long id) {
        log.debug("REST request to get Presidente : {}", id);
        Optional<Presidente> presidente = presidenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presidente);
    }

    /**
     * {@code DELETE  /presidentes/:id} : delete the "id" presidente.
     *
     * @param id the id of the presidente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presidentes/{id}")
    public ResponseEntity<Void> deletePresidente(@PathVariable Long id) {
        log.debug("REST request to delete Presidente : {}", id);
        presidenteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/presidentes?query=:query} : search for the presidente corresponding
     * to the query.
     *
     * @param query the query of the presidente search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/presidentes")
    public ResponseEntity<List<Presidente>> searchPresidentes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Presidentes for query {}", query);
        Page<Presidente> page = presidenteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
