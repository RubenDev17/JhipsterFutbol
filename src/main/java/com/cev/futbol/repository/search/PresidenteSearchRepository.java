package com.cev.futbol.repository.search;

import com.cev.futbol.domain.Presidente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Presidente} entity.
 */
public interface PresidenteSearchRepository extends ElasticsearchRepository<Presidente, Long> {
}
