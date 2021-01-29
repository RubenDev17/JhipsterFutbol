package com.cev.futbol.service;

import com.cev.futbol.domain.Equipo;
import com.cev.futbol.repository.EquipoRepository;
import com.cev.futbol.repository.search.EquipoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Equipo}.
 */
@Service
@Transactional
public class EquipoService {

    private final Logger log = LoggerFactory.getLogger(EquipoService.class);

    private final EquipoRepository equipoRepository;

    private final EquipoSearchRepository equipoSearchRepository;

    public EquipoService(EquipoRepository equipoRepository, EquipoSearchRepository equipoSearchRepository) {
        this.equipoRepository = equipoRepository;
        this.equipoSearchRepository = equipoSearchRepository;
    }

    /**
     * Save a equipo.
     *
     * @param equipo the entity to save.
     * @return the persisted entity.
     */
    public Equipo save(Equipo equipo) {
        log.debug("Request to save Equipo : {}", equipo);
        Equipo result = equipoRepository.save(equipo);
        equipoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the equipos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Equipo> findAll(Pageable pageable) {
        log.debug("Request to get all Equipos");
        return equipoRepository.findAll(pageable);
    }



    /**
     *  Get all the equipos where Presidente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<Equipo> findAllWherePresidenteIsNull() {
        log.debug("Request to get all equipos where Presidente is null");
        return StreamSupport
            .stream(equipoRepository.findAll().spliterator(), false)
            .filter(equipo -> equipo.getPresidente() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one equipo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Equipo> findOne(Long id) {
        log.debug("Request to get Equipo : {}", id);
        return equipoRepository.findById(id);
    }

    /**
     * Delete the equipo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Equipo : {}", id);
        equipoRepository.deleteById(id);
        equipoSearchRepository.deleteById(id);
    }

    /**
     * Search for the equipo corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Equipo> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Equipos for query {}", query);
        return equipoSearchRepository.search(queryStringQuery(query), pageable);    }
}
