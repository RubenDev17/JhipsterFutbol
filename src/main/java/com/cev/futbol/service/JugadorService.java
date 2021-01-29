package com.cev.futbol.service;

import com.cev.futbol.domain.Jugador;
import com.cev.futbol.repository.JugadorRepository;
import com.cev.futbol.repository.search.JugadorSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Jugador}.
 */
@Service
@Transactional
public class JugadorService {

    private final Logger log = LoggerFactory.getLogger(JugadorService.class);

    private final JugadorRepository jugadorRepository;

    private final JugadorSearchRepository jugadorSearchRepository;

    public JugadorService(JugadorRepository jugadorRepository, JugadorSearchRepository jugadorSearchRepository) {
        this.jugadorRepository = jugadorRepository;
        this.jugadorSearchRepository = jugadorSearchRepository;
    }

    /**
     * Save a jugador.
     *
     * @param jugador the entity to save.
     * @return the persisted entity.
     */
    public Jugador save(Jugador jugador) {
        log.debug("Request to save Jugador : {}", jugador);
        Jugador result = jugadorRepository.save(jugador);
        jugadorSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the jugadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Jugador> findAll(Pageable pageable) {
        log.debug("Request to get all Jugadors");
        return jugadorRepository.findAll(pageable);
    }


    /**
     * Get one jugador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Jugador> findOne(Long id) {
        log.debug("Request to get Jugador : {}", id);
        return jugadorRepository.findById(id);
    }

    /**
     * Delete the jugador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Jugador : {}", id);
        jugadorRepository.deleteById(id);
        jugadorSearchRepository.deleteById(id);
    }

    /**
     * Search for the jugador corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Jugador> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Jugadors for query {}", query);
        return jugadorSearchRepository.search(queryStringQuery(query), pageable);    }
}
