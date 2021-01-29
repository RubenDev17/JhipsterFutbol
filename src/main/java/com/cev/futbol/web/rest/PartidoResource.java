package com.cev.futbol.web.rest;

import com.cev.futbol.domain.Partido;
import com.cev.futbol.service.PartidoService;
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
 * REST controller for managing {@link com.cev.futbol.domain.Partido}.
 */
@RestController
@RequestMapping("/api")
public class PartidoResource {

    private final Logger log = LoggerFactory.getLogger(PartidoResource.class);

    private static final String ENTITY_NAME = "partido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartidoService partidoService;

    public PartidoResource(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    /**
     * {@code POST  /partidos} : Create a new partido.
     *
     * @param partido the partido to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partido, or with status {@code 400 (Bad Request)} if the partido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/partidos")
    public ResponseEntity<Partido> createPartido(@Valid @RequestBody Partido partido) throws URISyntaxException {
        log.debug("REST request to save Partido : {}", partido);
        if (partido.getId() != null) {
            throw new BadRequestAlertException("A new partido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Partido result = partidoService.save(partido);
        return ResponseEntity.created(new URI("/api/partidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /partidos} : Updates an existing partido.
     *
     * @param partido the partido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partido,
     * or with status {@code 400 (Bad Request)} if the partido is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/partidos")
    public ResponseEntity<Partido> updatePartido(@Valid @RequestBody Partido partido) throws URISyntaxException {
        log.debug("REST request to update Partido : {}", partido);
        if (partido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Partido result = partidoService.save(partido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, partido.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /partidos} : get all the partidos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partidos in body.
     */
    @GetMapping("/partidos")
    public ResponseEntity<List<Partido>> getAllPartidos(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Partidos");
        Page<Partido> page;
        if (eagerload) {
            page = partidoService.findAllWithEagerRelationships(pageable);
        } else {
            page = partidoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /partidos/:id} : get the "id" partido.
     *
     * @param id the id of the partido to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partido, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/partidos/{id}")
    public ResponseEntity<Partido> getPartido(@PathVariable Long id) {
        log.debug("REST request to get Partido : {}", id);
        Optional<Partido> partido = partidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partido);
    }

    /**
     * {@code DELETE  /partidos/:id} : delete the "id" partido.
     *
     * @param id the id of the partido to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/partidos/{id}")
    public ResponseEntity<Void> deletePartido(@PathVariable Long id) {
        log.debug("REST request to delete Partido : {}", id);
        partidoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/partidos?query=:query} : search for the partido corresponding
     * to the query.
     *
     * @param query the query of the partido search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/partidos")
    public ResponseEntity<List<Partido>> searchPartidos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Partidos for query {}", query);
        Page<Partido> page = partidoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
