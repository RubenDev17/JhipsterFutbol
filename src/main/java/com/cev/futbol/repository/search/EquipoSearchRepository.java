package com.cev.futbol.repository.search;

import com.cev.futbol.domain.Equipo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Equipo} entity.
 */
public interface EquipoSearchRepository extends ElasticsearchRepository<Equipo, Long> {
}
