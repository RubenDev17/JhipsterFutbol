package com.cev.futbol.service;

import com.cev.futbol.domain.Presidente;
import com.cev.futbol.repository.PresidenteRepository;
import com.cev.futbol.repository.search.PresidenteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Presidente}.
 */
@Service
@Transactional
public class PresidenteService {

    private final Logger log = LoggerFactory.getLogger(PresidenteService.class);

    private final PresidenteRepository presidenteRepository;

    private final PresidenteSearchRepository presidenteSearchRepository;

    public PresidenteService(PresidenteRepository presidenteRepository, PresidenteSearchRepository presidenteSearchRepository) {
        this.presidenteRepository = presidenteRepository;
        this.presidenteSearchRepository = presidenteSearchRepository;
    }

    /**
     * Save a presidente.
     *
     * @param presidente the entity to save.
     * @return the persisted entity.
     */
    public Presidente save(Presidente presidente) {
        log.debug("Request to save Presidente : {}", presidente);
        Presidente result = presidenteRepository.save(presidente);
        presidenteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the presidentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Presidente> findAll(Pageable pageable) {
        log.debug("Request to get all Presidentes");
        return presidenteRepository.findAll(pageable);
    }


    /**
     * Get one presidente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Presidente> findOne(Long id) {
        log.debug("Request to get Presidente : {}", id);
        return presidenteRepository.findById(id);
    }

    /**
     * Delete the presidente by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Presidente : {}", id);
        presidenteRepository.deleteById(id);
        presidenteSearchRepository.deleteById(id);
    }

    /**
     * Search for the presidente corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Presidente> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Presidentes for query {}", query);
        return presidenteSearchRepository.search(queryStringQuery(query), pageable);    }
}
