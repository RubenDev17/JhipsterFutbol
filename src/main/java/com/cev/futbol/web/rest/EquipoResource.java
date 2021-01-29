package com.cev.futbol.web.rest;

import com.cev.futbol.domain.Equipo;
import com.cev.futbol.service.EquipoService;
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
 * REST controller for managing {@link com.cev.futbol.domain.Equipo}.
 */
@RestController
@RequestMapping("/api")
public class EquipoResource {

    private final Logger log = LoggerFactory.getLogger(EquipoResource.class);

    private static final String ENTITY_NAME = "equipo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EquipoService equipoService;

    public EquipoResource(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    /**
     * {@code POST  /equipos} : Create a new equipo.
     *
     * @param equipo the equipo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new equipo, or with status {@code 400 (Bad Request)} if the equipo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/equipos")
    public ResponseEntity<Equipo> createEquipo(@Valid @RequestBody Equipo equipo) throws URISyntaxException {
        log.debug("REST request to save Equipo : {}", equipo);
        if (equipo.getId() != null) {
            throw new BadRequestAlertException("A new equipo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Equipo result = equipoService.save(equipo);
        return ResponseEntity.created(new URI("/api/equipos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /equipos} : Updates an existing equipo.
     *
     * @param equipo the equipo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated equipo,
     * or with status {@code 400 (Bad Request)} if the equipo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the equipo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/equipos")
    public ResponseEntity<Equipo> updateEquipo(@Valid @RequestBody Equipo equipo) throws URISyntaxException {
        log.debug("REST request to update Equipo : {}", equipo);
        if (equipo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Equipo result = equipoService.save(equipo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, equipo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /equipos} : get all the equipos.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of equipos in body.
     */
    @GetMapping("/equipos")
    public ResponseEntity<List<Equipo>> getAllEquipos(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("presidente-is-null".equals(filter)) {
            log.debug("REST request to get all Equipos where presidente is null");
            return new ResponseEntity<>(equipoService.findAllWherePresidenteIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Equipos");
        Page<Equipo> page = equipoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /equipos/:id} : get the "id" equipo.
     *
     * @param id the id of the equipo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the equipo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/equipos/{id}")
    public ResponseEntity<Equipo> getEquipo(@PathVariable Long id) {
        log.debug("REST request to get Equipo : {}", id);
        Optional<Equipo> equipo = equipoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(equipo);
    }

    /**
     * {@code DELETE  /equipos/:id} : delete the "id" equipo.
     *
     * @param id the id of the equipo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/equipos/{id}")
    public ResponseEntity<Void> deleteEquipo(@PathVariable Long id) {
        log.debug("REST request to delete Equipo : {}", id);
        equipoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/equipos?query=:query} : search for the equipo corresponding
     * to the query.
     *
     * @param query the query of the equipo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/equipos")
    public ResponseEntity<List<Equipo>> searchEquipos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Equipos for query {}", query);
        Page<Equipo> page = equipoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
