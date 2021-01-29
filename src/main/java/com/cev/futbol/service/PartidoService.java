package com.cev.futbol.service;

import com.cev.futbol.domain.Partido;
import com.cev.futbol.repository.PartidoRepository;
import com.cev.futbol.repository.search.PartidoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Partido}.
 */
@Service
@Transactional
public class PartidoService {

    private final Logger log = LoggerFactory.getLogger(PartidoService.class);

    private final PartidoRepository partidoRepository;

    private final PartidoSearchRepository partidoSearchRepository;

    public PartidoService(PartidoRepository partidoRepository, PartidoSearchRepository partidoSearchRepository) {
        this.partidoRepository = partidoRepository;
        this.partidoSearchRepository = partidoSearchRepository;
    }

    /**
     * Save a partido.
     *
     * @param partido the entity to save.
     * @return the persisted entity.
     */
    public Partido save(Partido partido) {
        log.debug("Request to save Partido : {}", partido);
        Partido result = partidoRepository.save(partido);
        partidoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the partidos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Partido> findAll(Pageable pageable) {
        log.debug("Request to get all Partidos");
        return partidoRepository.findAll(pageable);
    }


    /**
     * Get all the partidos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Partido> findAllWithEagerRelationships(Pageable pageable) {
        return partidoRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one partido by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Partido> findOne(Long id) {
        log.debug("Request to get Partido : {}", id);
        return partidoRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the partido by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Partido : {}", id);
        partidoRepository.deleteById(id);
        partidoSearchRepository.deleteById(id);
    }

    /**
     * Search for the partido corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Partido> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Partidos for query {}", query);
        return partidoSearchRepository.search(queryStringQuery(query), pageable);    }
}
