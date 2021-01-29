package com.cev.futbol.service;

import com.cev.futbol.domain.Dato;
import com.cev.futbol.repository.DatoRepository;
import com.cev.futbol.repository.search.DatoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Dato}.
 */
@Service
@Transactional
public class DatoService {

    private final Logger log = LoggerFactory.getLogger(DatoService.class);

    private final DatoRepository datoRepository;

    private final DatoSearchRepository datoSearchRepository;

    public DatoService(DatoRepository datoRepository, DatoSearchRepository datoSearchRepository) {
        this.datoRepository = datoRepository;
        this.datoSearchRepository = datoSearchRepository;
    }

    /**
     * Save a dato.
     *
     * @param dato the entity to save.
     * @return the persisted entity.
     */
    public Dato save(Dato dato) {
        log.debug("Request to save Dato : {}", dato);
        Dato result = datoRepository.save(dato);
        datoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the datoes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Dato> findAll(Pageable pageable) {
        log.debug("Request to get all Datoes");
        return datoRepository.findAll(pageable);
    }


    /**
     * Get one dato by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Dato> findOne(Long id) {
        log.debug("Request to get Dato : {}", id);
        return datoRepository.findById(id);
    }

    /**
     * Delete the dato by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dato : {}", id);
        datoRepository.deleteById(id);
        datoSearchRepository.deleteById(id);
    }

    /**
     * Search for the dato corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Dato> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Datoes for query {}", query);
        return datoSearchRepository.search(queryStringQuery(query), pageable);    }
}
