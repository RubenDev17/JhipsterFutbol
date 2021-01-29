package com.cev.futbol.repository.search;

import com.cev.futbol.domain.Temporada;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Temporada} entity.
 */
public interface TemporadaSearchRepository extends ElasticsearchRepository<Temporada, Long> {
}
