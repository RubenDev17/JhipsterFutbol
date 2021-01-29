package com.cev.futbol.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cev.futbol.domain.Jugador;
import com.cev.futbol.service.JugadorService;
import com.cev.futbol.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cev.futbol.domain.Jugador}.
 */
@RestController
@RequestMapping("/api")
public class JugadorResource {

    private final Logger log = LoggerFactory.getLogger(JugadorResource.class);

    private static final String ENTITY_NAME = "jugador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JugadorService jugadorService;

    public JugadorResource(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    /**
     * {@code POST  /jugadors} : Create a new jugador.
     *
     * @param jugador the jugador to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jugador, or with status {@code 400 (Bad Request)} if the jugador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jugadors")
    public ResponseEntity<Jugador> createJugador(@Valid @RequestBody Jugador jugador) throws URISyntaxException {
        log.debug("REST request to save Jugador : {}", jugador);
        if (jugador.getId() != null) {
            throw new BadRequestAlertException("A new jugador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Jugador result = jugadorService.save(jugador);
        return ResponseEntity.created(new URI("/api/jugadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jugadors} : Updates an existing jugador.
     *
     * @param jugador the jugador to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jugador,
     * or with status {@code 400 (Bad Request)} if the jugador is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jugador couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jugadors")
    public ResponseEntity<Jugador> updateJugador(@Valid @RequestBody Jugador jugador) throws URISyntaxException {
        log.debug("REST request to update Jugador : {}", jugador);
        if (jugador.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Jugador result = jugadorService.save(jugador);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jugador.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /jugadors} : get all the jugadors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jugadors in body.
     */
    @GetMapping("/jugadors")
    public ResponseEntity<List<Jugador>> getAllJugadors(Pageable pageable) {
        log.debug("REST request to get a page of Jugadors");
        Page<Jugador> page = jugadorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /jugadors/:id} : get the "id" jugador.
     *
     * @param id the id of the jugador to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jugador, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jugadors/{id}")
    public ResponseEntity<Jugador> getJugador(@PathVariable Long id) {
        log.debug("REST request to get Jugador : {}", id);
        Optional<Jugador> jugador = jugadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jugador);
    }

    /**
     * {@code DELETE  /jugadors/:id} : delete the "id" jugador.
     *
     * @param id the id of the jugador to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jugadors/{id}")
    public ResponseEntity<Void> deleteJugador(@PathVariable Long id) {
        log.debug("REST request to delete Jugador : {}", id);
        jugadorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/jugadors?query=:query} : search for the jugador corresponding
     * to the query.
     *
     * @param query the query of the jugador search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/jugadors")
    public ResponseEntity<List<Jugador>> searchJugadors(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Jugadors for query {}", query);
        Page<Jugador> page = jugadorService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    
    @GetMapping("/jugadorPorNombreAndEdad/{nombre}&{edad}")
    public List<Jugador> getJugadorByNombreAndEdad(@PathVariable String nombre,@PathVariable int edad) {
    	return jugadorService.getJugadorByNombreAndEdad(nombre, edad);
    }
    
    @GetMapping("/jugadorPorNombreOrEdad")
    public List<Jugador> getJugadorByNombreOrEdad(@RequestParam (required = false) String nombre,@RequestParam (required = false) Optional<Integer> edad) {
    	return jugadorService.getJugadorByNombreOrEdad(nombre, edad);
    }
    
    @GetMapping("/jugadoresConEdadEntre/{edadUno}&{edadDos}")
    public List<Jugador> getJugadorByEdadBetween(@PathVariable int edadUno, @PathVariable int edadDos){
    	return jugadorService.getJugadorByEdadBetween(edadUno, edadDos);
    }
}
