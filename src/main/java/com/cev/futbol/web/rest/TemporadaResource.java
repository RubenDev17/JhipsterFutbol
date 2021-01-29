package com.cev.futbol.web.rest;

import com.cev.futbol.domain.Temporada;
import com.cev.futbol.service.TemporadaService;
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
 * REST controller for managing {@link com.cev.futbol.domain.Temporada}.
 */
@RestController
@RequestMapping("/api")
public class TemporadaResource {

    private final Logger log = LoggerFactory.getLogger(TemporadaResource.class);

    private static final String ENTITY_NAME = "temporada";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemporadaService temporadaService;

    public TemporadaResource(TemporadaService temporadaService) {
        this.temporadaService = temporadaService;
    }

    /**
     * {@code POST  /temporadas} : Create a new temporada.
     *
     * @param temporada the temporada to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temporada, or with status {@code 400 (Bad Request)} if the temporada has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temporadas")
    public ResponseEntity<Temporada> createTemporada(@Valid @RequestBody Temporada temporada) throws URISyntaxException {
        log.debug("REST request to save Temporada : {}", temporada);
        if (temporada.getId() != null) {
            throw new BadRequestAlertException("A new temporada cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Temporada result = temporadaService.save(temporada);
        return ResponseEntity.created(new URI("/api/temporadas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temporadas} : Updates an existing temporada.
     *
     * @param temporada the temporada to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temporada,
     * or with status {@code 400 (Bad Request)} if the temporada is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temporada couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temporadas")
    public ResponseEntity<Temporada> updateTemporada(@Valid @RequestBody Temporada temporada) throws URISyntaxException {
        log.debug("REST request to update Temporada : {}", temporada);
        if (temporada.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Temporada result = temporadaService.save(temporada);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, temporada.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /temporadas} : get all the temporadas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temporadas in body.
     */
    @GetMapping("/temporadas")
    public ResponseEntity<List<Temporada>> getAllTemporadas(Pageable pageable) {
        log.debug("REST request to get a page of Temporadas");
        Page<Temporada> page = temporadaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /temporadas/:id} : get the "id" temporada.
     *
     * @param id the id of the temporada to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temporada, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temporadas/{id}")
    public ResponseEntity<Temporada> getTemporada(@PathVariable Long id) {
        log.debug("REST request to get Temporada : {}", id);
        Optional<Temporada> temporada = temporadaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(temporada);
    }

    /**
     * {@code DELETE  /temporadas/:id} : delete the "id" temporada.
     *
     * @param id the id of the temporada to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temporadas/{id}")
    public ResponseEntity<Void> deleteTemporada(@PathVariable Long id) {
        log.debug("REST request to delete Temporada : {}", id);
        temporadaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/temporadas?query=:query} : search for the temporada corresponding
     * to the query.
     *
     * @param query the query of the temporada search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/temporadas")
    public ResponseEntity<List<Temporada>> searchTemporadas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Temporadas for query {}", query);
        Page<Temporada> page = temporadaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
