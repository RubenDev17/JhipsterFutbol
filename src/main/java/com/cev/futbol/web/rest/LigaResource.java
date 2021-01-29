package com.cev.futbol.web.rest;

import com.cev.futbol.domain.Liga;
import com.cev.futbol.service.LigaService;
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
 * REST controller for managing {@link com.cev.futbol.domain.Liga}.
 */
@RestController
@RequestMapping("/api")
public class LigaResource {

    private final Logger log = LoggerFactory.getLogger(LigaResource.class);

    private static final String ENTITY_NAME = "liga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigaService ligaService;

    public LigaResource(LigaService ligaService) {
        this.ligaService = ligaService;
    }

    /**
     * {@code POST  /ligas} : Create a new liga.
     *
     * @param liga the liga to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new liga, or with status {@code 400 (Bad Request)} if the liga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ligas")
    public ResponseEntity<Liga> createLiga(@Valid @RequestBody Liga liga) throws URISyntaxException {
        log.debug("REST request to save Liga : {}", liga);
        if (liga.getId() != null) {
            throw new BadRequestAlertException("A new liga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Liga result = ligaService.save(liga);
        return ResponseEntity.created(new URI("/api/ligas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ligas} : Updates an existing liga.
     *
     * @param liga the liga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liga,
     * or with status {@code 400 (Bad Request)} if the liga is not valid,
     * or with status {@code 500 (Internal Server Error)} if the liga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ligas")
    public ResponseEntity<Liga> updateLiga(@Valid @RequestBody Liga liga) throws URISyntaxException {
        log.debug("REST request to update Liga : {}", liga);
        if (liga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Liga result = ligaService.save(liga);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, liga.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ligas} : get all the ligas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligas in body.
     */
    @GetMapping("/ligas")
    public ResponseEntity<List<Liga>> getAllLigas(Pageable pageable) {
        log.debug("REST request to get a page of Ligas");
        Page<Liga> page = ligaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligas/:id} : get the "id" liga.
     *
     * @param id the id of the liga to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the liga, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ligas/{id}")
    public ResponseEntity<Liga> getLiga(@PathVariable Long id) {
        log.debug("REST request to get Liga : {}", id);
        Optional<Liga> liga = ligaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(liga);
    }

    /**
     * {@code DELETE  /ligas/:id} : delete the "id" liga.
     *
     * @param id the id of the liga to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ligas/{id}")
    public ResponseEntity<Void> deleteLiga(@PathVariable Long id) {
        log.debug("REST request to delete Liga : {}", id);
        ligaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ligas?query=:query} : search for the liga corresponding
     * to the query.
     *
     * @param query the query of the liga search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ligas")
    public ResponseEntity<List<Liga>> searchLigas(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Ligas for query {}", query);
        Page<Liga> page = ligaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
