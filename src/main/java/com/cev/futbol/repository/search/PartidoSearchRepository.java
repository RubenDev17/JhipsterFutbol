package com.cev.futbol.repository.search;

import com.cev.futbol.domain.Partido;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Partido} entity.
 */
public interface PartidoSearchRepository extends ElasticsearchRepository<Partido, Long> {
}
