package com.cev.futbol.web.rest;

import com.cev.futbol.domain.Dato;
import com.cev.futbol.service.DatoService;
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
 * REST controller for managing {@link com.cev.futbol.domain.Dato}.
 */
@RestController
@RequestMapping("/api")
public class DatoResource {

    private final Logger log = LoggerFactory.getLogger(DatoResource.class);

    private static final String ENTITY_NAME = "dato";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DatoService datoService;

    public DatoResource(DatoService datoService) {
        this.datoService = datoService;
    }

    /**
     * {@code POST  /datoes} : Create a new dato.
     *
     * @param dato the dato to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dato, or with status {@code 400 (Bad Request)} if the dato has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/datoes")
    public ResponseEntity<Dato> createDato(@Valid @RequestBody Dato dato) throws URISyntaxException {
        log.debug("REST request to save Dato : {}", dato);
        if (dato.getId() != null) {
            throw new BadRequestAlertException("A new dato cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dato result = datoService.save(dato);
        return ResponseEntity.created(new URI("/api/datoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /datoes} : Updates an existing dato.
     *
     * @param dato the dato to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dato,
     * or with status {@code 400 (Bad Request)} if the dato is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dato couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/datoes")
    public ResponseEntity<Dato> updateDato(@Valid @RequestBody Dato dato) throws URISyntaxException {
        log.debug("REST request to update Dato : {}", dato);
        if (dato.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Dato result = datoService.save(dato);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, dato.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /datoes} : get all the datoes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of datoes in body.
     */
    @GetMapping("/datoes")
    public ResponseEntity<List<Dato>> getAllDatoes(Pageable pageable) {
        log.debug("REST request to get a page of Datoes");
        Page<Dato> page = datoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /datoes/:id} : get the "id" dato.
     *
     * @param id the id of the dato to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dato, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/datoes/{id}")
    public ResponseEntity<Dato> getDato(@PathVariable Long id) {
        log.debug("REST request to get Dato : {}", id);
        Optional<Dato> dato = datoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dato);
    }

    /**
     * {@code DELETE  /datoes/:id} : delete the "id" dato.
     *
     * @param id the id of the dato to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/datoes/{id}")
    public ResponseEntity<Void> deleteDato(@PathVariable Long id) {
        log.debug("REST request to delete Dato : {}", id);
        datoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/datoes?query=:query} : search for the dato corresponding
     * to the query.
     *
     * @param query the query of the dato search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/datoes")
    public ResponseEntity<List<Dato>> searchDatoes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Datoes for query {}", query);
        Page<Dato> page = datoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
