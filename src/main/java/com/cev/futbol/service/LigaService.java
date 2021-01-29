package com.cev.futbol.service;

import com.cev.futbol.domain.Liga;
import com.cev.futbol.repository.LigaRepository;
import com.cev.futbol.repository.search.LigaSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Liga}.
 */
@Service
@Transactional
public class LigaService {

    private final Logger log = LoggerFactory.getLogger(LigaService.class);

    private final LigaRepository ligaRepository;

    private final LigaSearchRepository ligaSearchRepository;

    public LigaService(LigaRepository ligaRepository, LigaSearchRepository ligaSearchRepository) {
        this.ligaRepository = ligaRepository;
        this.ligaSearchRepository = ligaSearchRepository;
    }

    /**
     * Save a liga.
     *
     * @param liga the entity to save.
     * @return the persisted entity.
     */
    public Liga save(Liga liga) {
        log.debug("Request to save Liga : {}", liga);
        Liga result = ligaRepository.save(liga);
        ligaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ligas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Liga> findAll(Pageable pageable) {
        log.debug("Request to get all Ligas");
        return ligaRepository.findAll(pageable);
    }


    /**
     * Get one liga by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Liga> findOne(Long id) {
        log.debug("Request to get Liga : {}", id);
        return ligaRepository.findById(id);
    }

    /**
     * Delete the liga by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Liga : {}", id);
        ligaRepository.deleteById(id);
        ligaSearchRepository.deleteById(id);
    }

    /**
     * Search for the liga corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Liga> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Ligas for query {}", query);
        return ligaSearchRepository.search(queryStringQuery(query), pageable);    }
}
