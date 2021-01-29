package com.cev.futbol.service;

import com.cev.futbol.domain.Temporada;
import com.cev.futbol.repository.TemporadaRepository;
import com.cev.futbol.repository.search.TemporadaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Temporada}.
 */
@Service
@Transactional
public class TemporadaService {

    private final Logger log = LoggerFactory.getLogger(TemporadaService.class);

    private final TemporadaRepository temporadaRepository;

    private final TemporadaSearchRepository temporadaSearchRepository;

    public TemporadaService(TemporadaRepository temporadaRepository, TemporadaSearchRepository temporadaSearchRepository) {
        this.temporadaRepository = temporadaRepository;
        this.temporadaSearchRepository = temporadaSearchRepository;
    }

    /**
     * Save a temporada.
     *
     * @param temporada the entity to save.
     * @return the persisted entity.
     */
    public Temporada save(Temporada temporada) {
        log.debug("Request to save Temporada : {}", temporada);
        Temporada result = temporadaRepository.save(temporada);
        temporadaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the temporadas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Temporada> findAll(Pageable pageable) {
        log.debug("Request to get all Temporadas");
        return temporadaRepository.findAll(pageable);
    }


    /**
     * Get one temporada by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Temporada> findOne(Long id) {
        log.debug("Request to get Temporada : {}", id);
        return temporadaRepository.findById(id);
    }

    /**
     * Delete the temporada by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Temporada : {}", id);
        temporadaRepository.deleteById(id);
        temporadaSearchRepository.deleteById(id);
    }

    /**
     * Search for the temporada corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Temporada> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Temporadas for query {}", query);
        return temporadaSearchRepository.search(queryStringQuery(query), pageable);    }
}
